package com.lamoroso.example

import com.lamoroso.example.routes.TestRoute
import zhttp.http.*
import zhttp.http.middleware.HttpMiddleware
import zhttp.service.Server as ZhttpServer
import zio.Random
import zio.ZIO
import zio.ZLayer
final case class Server(
    testRoute: TestRoute
):
  val allRoutes: HttpApp[Any, Throwable] = testRoute.routes

  val loggingMiddleware: HttpMiddleware[Any, Nothing] =
    new HttpMiddleware[Any, Nothing] {
      override def apply[R1 <: Any, E1 >: Nothing](
          http: Http[R1, E1, Request, Response]
      ): Http[R1, E1, Request, Response] =
        Http.fromOptionFunction[Request] { request =>
          Random.nextUUID.flatMap { requestId =>
            ZIO.logAnnotate("rid", requestId.toString) {
              http(request)
            }
          }
        }
    }

  def start: ZIO[Any, Throwable, Unit] =
    for {
      _ <- ZIO.logInfo(s"Starting migrations ...")
      _ <- ZIO.logInfo(
        s"Starting server at: http://localhost:8080/ ..."
      )
      _ <- ZhttpServer
        .start(8080, allRoutes @@ loggingMiddleware)
    } yield ()

object Server:

  val layer = ZLayer.fromFunction(Server.apply _)
