package com.lamoroso.example

import zio.*

import zio.logging.backend.SLF4J
import zio.logging.removeDefaultLoggers
import com.lamoroso.example.Server
import com.lamoroso.example.routes.TestRoute
import zio.http.ServerConfig
import zio.http.Server as ZIOHttpServer
import com.lamoroso.example.service.TestService

object Main extends ZIOAppDefault:

  override def run: ZIO[Any, Any, Any] =
    ZIO
      .serviceWithZIO[Server](_.start)
      .provide(
        TestService.layer,
        ServerConfig.live(ServerConfig.default.port(8080)),
        ZIOHttpServer.live,
        Server.layer,
        SLF4J.slf4j,
        removeDefaultLoggers
      )
