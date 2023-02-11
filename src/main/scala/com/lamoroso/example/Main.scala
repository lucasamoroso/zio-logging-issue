package com.lamoroso.example

import zio.*

import zio.logging.backend.SLF4J
import zio.logging.removeDefaultLoggers
import com.lamoroso.example.Server
import com.lamoroso.example.routes.TestRoute

import com.lamoroso.example.routes.TestRoute

import com.lamoroso.example.Server
object Main extends ZIOAppDefault:

  override def run: ZIO[Any, Any, Any] =
    ZIO
      .serviceWithZIO[Server](_.start)
      .provide(
        Server.layer,
        TestRoute.layer,
        SLF4J.slf4j,
        removeDefaultLoggers
      )
