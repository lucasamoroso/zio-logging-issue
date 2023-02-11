package com.lamoroso.example

import com.lamoroso.example.routes.TestRoute
import com.lamoroso.example.endpoints.TestRoutesEndpoints

import com.lamoroso.example.service.TestService
import sttp.tapir.model.ServerRequest
import sttp.tapir.server.interceptor.RequestInterceptor
import sttp.tapir.server.interceptor.RequestInterceptor.RequestResultEffectTransform
import sttp.tapir.server.interceptor.RequestResult
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.server.ziohttp.ZioHttpServerOptions
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.*
import zio.Random
import zio.ZLayer
import zio.*
import zio.http.App
import zio.http.ServerConfig
import zio.http.{Server => ZIOHttpServer}

final case class Server():
  type RoutesEnv = TestService

//TODO: Add middleware to:
// - log error responses and defects

  lazy val requestIdInterceptor = RequestInterceptor.transformResultEffect(
    new RequestResultEffectTransform[Task] {
      override def apply[B](
          request: ServerRequest,
          result: Task[RequestResult[B]]
      ): Task[RequestResult[B]] =
        Random.nextUUID.flatMap { requestId =>
          ZIO.logAnnotate("rid", requestId.toString) {
            result
          }
        }
    }
  )

  val decodeFailureHandler = DefaultDecodeFailureHandler.default.copy(
    respond = DefaultDecodeFailureHandler.respond(
      _,
      badRequestOnPathErrorIfPathShapeMatches = true,
      badRequestOnPathInvalidIfPathShapeMatches = true
    )
  )

  // Docs
  lazy val swaggerEndpoints =
    SwaggerInterpreter()
      .fromEndpoints[Task](
        List(TestRoutesEndpoints.listEndpoint),
        "Subscriptions app",
        "1.0"
      )

  //  Build all server routes
  lazy val routes: App[RoutesEnv] =
    ZioHttpInterpreter(
      ZioHttpServerOptions.customiseInterceptors
        .prependInterceptor(requestIdInterceptor)
        .decodeFailureHandler(decodeFailureHandler)
        .options
    )
      .toApp(
        List(TestRoute.listServerEndpoint).map(_.widen[RoutesEnv])
          ++
            swaggerEndpoints.map(_.widen[RoutesEnv])
      )

  def start =
    for {
      _ <- ZIO.logInfo(
        s"Starting docs service at: http://localhost:8080/docs ..."
      )
      _ <- ZIOHttpServer
        .serve(routes)
    } yield ()

object Server:

  val layer = ZLayer.fromFunction(Server.apply _)
