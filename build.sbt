val slf4jVersion = "2.0.5" // logging framework
val logbackVersion = "1.4.5" //logging framework implementation
val jansiVersion = "1.18" // Colored log lines
val zhttpVersion = "2.0.0-RC11" // HTTP client library for ZIO
val zioJsonVersion = "0.4.2" // JSON serialization library for ZIO
val zioLoggingVersion = "2.1.8" // logging library for ZIO
val zioVersion =
  "2.0.7" // Scala library for asynchronous and concurrent programming

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

ThisBuild / fork := true

lazy val root = (project in file("."))
  .settings(
    name := "zio-logging-issue",
    scalacOptions ++= Seq(
      "-Wunused:all"
    ),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "io.d11" %% "zhttp" % zhttpVersion,
      "dev.zio" %% "zio-json" % zioJsonVersion,
      "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion,
      "org.slf4j" % "slf4j-api" % slf4jVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "org.fusesource.jansi" % "jansi" % jansiVersion
    )
  )
