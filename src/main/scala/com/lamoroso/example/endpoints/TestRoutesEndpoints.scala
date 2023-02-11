package com.lamoroso.example.endpoints

import sttp.model.StatusCode
import sttp.tapir.Endpoint
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*

object TestRoutesEndpoints:
  val listEndpoint =
    endpoint.get
      .out(jsonBody[String])
      .errorOut(
        oneOf(oneOfVariant(statusCode(StatusCode.InternalServerError)))
      )
