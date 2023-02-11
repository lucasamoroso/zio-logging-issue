package com.lamoroso.example.routes
import com.lamoroso.example.service.TestService
import zhttp.http.*
import zio.*
import zio.json.*

final case class TestRoute(service: TestService):
  val routes: Http[Any, Throwable, Request, Response] =
    Http.collectZIO[Request] { case Method.GET -> !! =>
      for {
        _ <- ZIO.logInfo("Calling service  ...")
        result <- service.list()
        _ <- ZIO.logInfo(s"Got a result ${result} from service  ...")
      } yield Response.text("Ok")
    }

object TestRoute:

  val layer = ZLayer.fromFunction(TestRoute.apply _)
