package com.performantdata.voucher

import cats.effect.IO
import com.performantdata.voucher.Library.*
import io.circe.generic.auto.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object Endpoints {
  private[this] case class User(name: String) extends AnyVal

  val helloServerEndpoint: ServerEndpoint[Any, IO] = {
    val helloEndpoint: PublicEndpoint[User, Unit, String, Any] =
      endpoint.get.in("hello").in(query[User]("name")).out(stringBody)
    helloEndpoint.serverLogicSuccess(user => IO.pure(s"Hello ${user.name}"))
  }

  val booksListingServerEndpoint: ServerEndpoint[Any, IO] = {
    val booksListing: PublicEndpoint[Unit, Unit, List[Book], Any] =
      endpoint.get.in("books" / "list" / "all").out(jsonBody[List[Book]])
    booksListing.serverLogicSuccess(_ => IO.pure(Library.books))
  }

  val prometheusMetrics: PrometheusMetrics[IO] = PrometheusMetrics.default[IO]()

  val all: List[ServerEndpoint[Any, IO]] = {
    val apiEndpoints = helloServerEndpoint :: booksListingServerEndpoint :: Nil
    val docEndpoints = SwaggerInterpreter().fromServerEndpoints[IO](apiEndpoints, BuildInfo.name, BuildInfo.version)

    apiEndpoints ::: docEndpoints ::: prometheusMetrics.metricsEndpoint :: Nil
  }
}
