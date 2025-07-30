ThisBuild / scalaVersion     := "3.3.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.d3javu"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "backend",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.19",
      "dev.zio" %% "zio-test" % "2.1.19" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

libraryDependencies += "dev.zio" %% "zio-kafka" % "3.0.0"
libraryDependencies += "dev.zio" %% "zio-http" % "3.3.3"
libraryDependencies += "dev.zio" %% "zio-json" % "0.7.44"
libraryDependencies += "dev.zio" %% "zio-config" % "4.0.4"
libraryDependencies += "dev.zio" %% "zio-config-typesafe" % "4.0.4"
libraryDependencies += "dev.zio" %% "zio-config-magnolia" % "4.0.4"
libraryDependencies += "dev.zio" %% "zio-config-refined" % "4.0.4"
libraryDependencies += "dev.zio" %% "zio-schema-json" % "1.7.3"

libraryDependencies += "io.jsonwebtoken" % "jjwt-api" % "0.12.6"
libraryDependencies += "io.jsonwebtoken" % "jjwt-impl" % "0.12.6" % "runtime"
libraryDependencies += "io.jsonwebtoken" % "jjwt-jackson" % "0.12.6" % "runtime"

libraryDependencies += "org.postgresql" % "postgresql" % "42.7.7"

libraryDependencies += "org.springframework.security" % "spring-security-crypto" % "6.5.0"
libraryDependencies += "commons-logging" % "commons-logging" % "1.3.5"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.18.0"

libraryDependencies += "io.getquill" %% "quill-jdbc" % "4.8.6"
libraryDependencies += "io.getquill" %% "quill-jdbc-zio" % "4.8.6"
libraryDependencies += "io.getquill" %% "quill-cassandra" % "4.8.6"
libraryDependencies += "io.getquill" %% "quill-cassandra-zio" % "4.8.6"
libraryDependencies += "io.getquill" %% "quill-caliban" % "4.8.6"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "2.0.17"

libraryDependencies ++= Seq(
  "io.github.zeal18" %% "zio-mongodb-bson" % "0.11.1",
  "io.github.zeal18" %% "zio-mongodb-driver" % "0.11.1",
  "io.github.zeal18" %% "zio-mongodb-testkit" % "0.11.1" % Test
)

Compile / PB.targets := Seq(
  scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value / "scalapb",
  scalapb.gen(grpc = true) -> (Compile / sourceManaged).value / "scalapb",
)

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % "1.50.1",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
)