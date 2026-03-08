import sbt.util

import Dependencies.*

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.18"

lazy val root = (project.in(file("."))).settings(
  name := "backend",
  idePackagePrefix := Some("org.d3javu"),
  libraryDependencies ++= dependencies,
  scalacOptions ++= Seq("-Ymacro-annotations", "-Ymacro-expand:normal"),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
//  addCompilerPlugin(
//    "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full,
//  ),
)

dockerBaseImage := "eclipse-temurin:21-jre-alpine-3.23"
dockerExposedPorts := Seq(8080, 9090)
Docker / version := "1.0.0"

resolvers += "confluent".at("https://packages.confluent.io/maven/")

enablePlugins(Fs2Grpc)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
