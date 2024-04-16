package com.performantdata.voucher

import caliban.interop.tapir.{HttpInterpreter, WebSocketInterpreter}
import caliban.{CalibanError, Http4sAdapter}
import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.*
import org.http4s.{HttpRoutes, StaticFile}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

/** HTTP server.
  *
  * Runs on port 9000, to match the Docker image's `EXPOSE`.
  * Listens on all IPv4 interfaces, as would be required for an exposed Docker process.
  * (We choose IPv4 because using IPv6 inside Docker containers is still not mainstream.)
  */
object Main extends IOApp {
  /** The HTTP request routes. */
  private[this] val routes: HttpRoutes[IO] = {
    val serverOptions: Http4sServerOptions[IO] =
      Http4sServerOptions.customiseInterceptors[IO]
        .metricsInterceptor(Endpoints.prometheusMetrics.metricsInterceptor())
        .options

    Http4sServerInterpreter[IO](serverOptions).toRoutes(Endpoints.all)
  }

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder.default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"9000")
      .withHttpWebSocketApp { wsBuilder =>
        Router[IO](
          "/api/graphql" ->
            CORS.policy(Http4sAdapter.makeHttpServiceF[IO, Any, CalibanError](HttpInterpreter(interpreter))),
          "/ws/graphql"  ->
            CORS.policy(Http4sAdapter.makeWebSocketServiceF[IO, Any, CalibanError](wsBuilder, WebSocketInterpreter(interpreter))),
          "/graphiql"    ->
            Kleisli.liftF(StaticFile.fromResource("/graphiql.html", None))
        ).orNotFound
      }
      .withHttpApp(Router("/" -> routes).orNotFound)
      .build
      .useForever
      .as(ExitCode.Success)
}
