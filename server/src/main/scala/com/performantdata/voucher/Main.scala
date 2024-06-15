package com.performantdata.voucher

import caliban.{CalibanError, GraphQLInterpreter, Http4sAdapter}
import cats.effect.kernel.Sync
import cats.effect.std.Dispatcher
import cats.effect.{Async, ExitCode, IO, IOApp}
import com.comcast.ip4s.*
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.{HttpApp, HttpRoutes, Response, StaticFile}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

/** HTTP server.
  *
  * Runs on port 9000, to match the Docker image's `EXPOSE`.
  * Listens on all IPv4 interfaces, as would be required for an exposed Docker process.
  * (We choose IPv4 because using IPv6 inside Docker containers is still not mainstream.)
  *
  * Can be interrupted by a SIGINT, like any normal [[IOApp `IOApp`]].
  *
  * @see [[IOApp]]
  */
object Main extends IOApp {
  private implicit def logger[F[_] : Sync]: Logger[F] = Slf4jLogger.getLogger[F]

  private implicit val zioRuntime: zio.Runtime[Any] = zio.Runtime.default
/*TODO A more-specific environment may be more correct.
 * @see https://github.com/ghostdogpr/caliban/blob/series/2.x/examples/src/main/scala/example/http4s/ExampleAppF.scala
  …: Runtime[ExampleService] =
    Unsafe.unsafe(implicit u => Runtime.unsafe.fromLayer(ExampleService.make(sampleCharacters)))
 */

  /** The http4s request routes.
    *
    * @tparam F an effect type
    */
  private def
  routes[F[_] : Async](graphqlInterpreter: GraphQLInterpreter[Any, CalibanError])(wsBuilder: WebSocketBuilder2[F])(implicit dispatcher: Dispatcher[F]): HttpApp[F] = {
    import caliban.interop.tapir.{HttpInterpreter, WebSocketInterpreter}
    import cats.data.Kleisli
    import sttp.tapir.json.circe.circeCodec
    import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

    /** Options for the http4s server that add a metrics interceptor.
      *
      * @see [[https://tapir.softwaremill.com/en/latest/server/options.html]]
      * @see [[https://tapir.softwaremill.com/en/latest/server/interceptors.html]]
      */
    val serverOptions: Http4sServerOptions[F] =
      Http4sServerOptions.customiseInterceptors[F]
        .metricsInterceptor(Endpoints.prometheusMetrics.metricsInterceptor())
        .options

    /** Tapir interpreter for the http4s server, including options.
      *
      * @see [[https://tapir.softwaremill.com/en/latest/server/http4s.html]]
      */
    val serverInterpreter = Http4sServerInterpreter[F](serverOptions)
    //TODO This should probably wrap *all* the endpoints. Check what other middleware is available.

    val httpInterpreter = HttpInterpreter(graphqlInterpreter)
    val wsInterpreter   = WebSocketInterpreter(graphqlInterpreter)

    Router[F](
      "/graphql"    -> CORS.policy(Http4sAdapter.makeHttpServiceF[F, Any, CalibanError](httpInterpreter)),
      "/ws/graphql" -> CORS.policy(Http4sAdapter.makeWebSocketServiceF[F, Any, CalibanError](wsBuilder, wsInterpreter)),
      "/graphiql"   -> Kleisli.liftF(StaticFile.fromResource("/graphiql.html", None)),
      "/"           -> serverInterpreter.toRoutes(Endpoints.all[F])
    ).orNotFound
  }

  /** Run the server.
    * 
    * If our GraphQL schema (defined in the Scala code) validates,
    * build and run a server that listens on all interfaces, on TCP port 9000;
    * otherwise, log the schema error.
    * 
    * @param args ignored
    * @return UNIX process exit code: 1 for a schema error
    */
  override def run(args: List[String]): IO[ExitCode] = {
    import caliban.interop.cats.implicits._
    import com.performantdata.voucher.schema.GraphqlSchema
    import org.http4s.ember.server.EmberServerBuilder

    // Parse the schema, to get a GraphQL interpreter.
    GraphqlSchema.api.interpreterF[IO].flatMap { graphqlInterpreter =>
      /** The Slick database API. */
      //database: Resource[IO, Database] = Resource.make()()

      Dispatcher.parallel[IO].use { implicit dispatcher =>
        /** The http4s Ember server. */
        val server = EmberServerBuilder.default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"9000")
          .withHttpWebSocketApp(routes[IO](graphqlInterpreter))
          .build

        server.useForever.as(ExitCode.Success)
      }
    }
  }
}
