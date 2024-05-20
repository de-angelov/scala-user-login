val scala3Version = "3.3.3"
val zioConfigVersion = "4.0.0-RC16"
val zioLoggingVersion = "2.1.16"

val config = Seq(
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion
)

val utils = Seq(
  "dev.zio" %% "zio-logging" % zioLoggingVersion,
  "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion,
  "com.github.jwt-scala" %% "jwt-core"  % "9.1.1"
)

val http = Seq(
  "dev.zio" %% "zio-http" % "3.0.0-RC6"
)

val db = Seq(
  "io.getquill" %% "quill-jdbc-zio" % "4.8.4",
  "org.postgresql" % "postgresql"  % "42.6.2",
  "org.flywaydb" % "flyway-core" % "10.13.0",
  "com.zaxxer" % "HikariCP" % "5.1.0",
)

lazy val rootProject
  = (project in file("."))
  .settings(
    Seq(
      name := "scala-user-login",
      version := "0.1.0-SNAPSHOT",
      organization := "de-angelov",
      scalaVersion := scala3Version,
      scalacOptions ++= Seq(
        "-Xmax-inlines",
        "64"
      ),
      libraryDependencies ++= config ++ utils ++ http ++ db
    )
  )




