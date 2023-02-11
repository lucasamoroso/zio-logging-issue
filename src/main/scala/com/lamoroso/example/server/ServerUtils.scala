package com.lamoroso.example

import zio.*

import zio.json.*

import zhttp.http.Request

object ServerUtils:

  def parseBody[A: JsonDecoder](request: Request): Task[A] =
    for {
      body <- request.body.asString
        .logError(s"The body ${request.body} provided by the user was empty")
      parsed <- ZIO
        .from(body.fromJson[A])
        .logError(s"The body $body provided by the user is not deserializable")
        .mapError(_ => new RuntimeException("boom"))
    } yield parsed
