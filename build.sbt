val scala3Version = "3.4.1"
val zioConfigVersion = "4.0.0-RC16"
val zioLoggingVersion = "2.1.16"

val config = Seq(
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion
)

val utils = Seq(
  "dev.zio" %% "zio-logging" % zioLoggingVersion,
  "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion
)


val html = Seq(
  "com.lihaoyi" %% "scalatags" % "0.12.0"
)


val http = Seq(
  "dev.zio" %% "zio-http" % "3.0.0-RC4"
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-user-login",
    version := "0.1.0-SNAPSHOT",
    scalacOptions ++= Seq(
      "-Xmax-inlines",
      "64"
    ),

    scalaVersion := scala3Version,

    libraryDependencies ++=  html ++ http ++ config ++ utils
  )
