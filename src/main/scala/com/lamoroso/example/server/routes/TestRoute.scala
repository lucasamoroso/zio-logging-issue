package com.lamoroso.example.routes
import zhttp.http.*
import zio.*
import zio.json.*

final case class TestRoute():
  val routes: Http[Any, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      // Return all subscriptions
      case Method.GET -> !! =>
        ZIO.logInfo("Executing request ...") *>
          ZIO.sleep(500.millis) *>
          ZIO.logInfo("Preparing response") *>
          ZIO.succeed(Response.text("Ok"))
    }

object TestRoute:

  val layer = ZLayer.fromFunction(TestRoute.apply _)
