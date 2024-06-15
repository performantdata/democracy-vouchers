package com.performantdata.voucher

import cats.Applicative
import com.performantdata.voucher.Library.*
import io.circe.generic.auto.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.swagger.bundle.SwaggerInterpreter

/** Representations of the HTTP server endpoints, expressed as Tapir objects.
  * 
  */
object Endpoints {
  private case class User(name: String) extends AnyVal

  def helloServerEndpoint[F[_]](using ap: Applicative[F]): ServerEndpoint[Any, F] = {
    val helloEndpoint: PublicEndpoint[User, Unit, String, Any] =
      endpoint.get.in("hello").in(query[User]("name")).out(stringBody)
    helloEndpoint.serverLogicSuccess(user => ap.pure(s"Hello ${user.name}"))
  }

  def booksListingServerEndpoint[F[_]](using ap: Applicative[F]): ServerEndpoint[Any, F] = {
    val booksListing: PublicEndpoint[Unit, Unit, List[Book], Any] =
      endpoint.get.in("books" / "list" / "all").out(jsonBody[List[Book]])
    booksListing.serverLogicSuccess(_ => ap.pure(Library.books))
  }

  def prometheusMetrics[F[_]]: PrometheusMetrics[F] = PrometheusMetrics.default[F]()

  /** All of the HTTP server endpoints.
    * Includes those for documentation, monitoring, etc.
    *
    * @tparam F an effect type
    */
  def all[F[_] : Applicative]: List[ServerEndpoint[Any, F]] = {
    /** Collected API endpoints. */
    val apiEndpoints = helloServerEndpoint[F] :: booksListingServerEndpoint[F] :: Nil
    
    /** OpenAPI documentation endpoint.
      * @see [[https://tapir.softwaremill.com/en/latest/docs/openapi.html]]
      */
    val docEndpoints = SwaggerInterpreter().fromServerEndpoints[F](apiEndpoints, BuildInfo.name, BuildInfo.version)

    apiEndpoints ::: docEndpoints ::: prometheusMetrics.metricsEndpoint :: Nil
  }
}
