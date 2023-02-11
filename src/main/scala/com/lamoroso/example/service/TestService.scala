package com.lamoroso.example.service

import zio.*

case class TestService():
  def list() =
    ZIO.logInfo("Listing all ...") *>
      ZIO.sleep(500.millis) *>
      ZIO.logInfo("Preparing to return") *>
      ZIO.succeed("Ok")

object TestService:
  val layer = ZLayer.fromFunction(TestService.apply _)
