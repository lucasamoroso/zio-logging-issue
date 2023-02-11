package com.lamoroso.example.routes
import com.lamoroso.example.server.endpoints.TestRoutesEndpoints.listEndpoint
import sttp.tapir.ztapir
import sttp.tapir.ztapir.*
import zio.*
import zio.json.*
import com.lamoroso.example.service.TestService

object TestRoute:
  val listServerEndpoint =
    listEndpoint.zServerLogic { _ =>
      for {
        service <- ZIO.service[TestService]
        _ <- ZIO.logInfo("Calling service  ...")
        result <- service.list()
        _ <- ZIO.logInfo(s"Got a result ${result} from service  ...")
      } yield result

    }
