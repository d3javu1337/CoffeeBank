import sbt._

object Dependencies {

  val testDependencies: Seq[ModuleID] = {

    val scalamockVersion = "7.5.5"
    val scalatestVersion = "3.2.19"

    Seq(
      "org.scalamock" %% "scalamock" % scalamockVersion % Test,
      "org.scalamock" %% "scalamock-cats-effect" % scalamockVersion % Test,
      "org.mockito" % "mockito-scala_2.13" % "2.0.0" % Test
    ) ++ Seq(
      "org.scalactic" %% "scalactic" % scalatestVersion,
      "org.scalatest" %% "scalatest" % scalatestVersion % "test"
    )
  }

  val cats: Seq[ModuleID] = {
    Seq(
      "org.typelevel" %% "cats-effect" % "3.6.3",
      "org.typelevel" %% "cats-core" % "2.13.0",
      "org.typelevel" %% "cats-mtl" % "1.6.0",
    )
  }

  val util: Seq[ModuleID] = {
    Seq(
      "io.estatico" %% "newtype" % "0.4.4",
      "com.beachape" %% "enumeratum" % "1.9.2",
      "com.beachape" %% "enumeratum-cats" % "1.9.2",
      "com.github.pureconfig" %% "pureconfig" % "0.17.9",
    ) ++ {
      val circeVersion = "0.14.15"

      Seq(
        "io.circe" %% "circe-core",
        "io.circe" %% "circe-generic",
        "io.circe" %% "circe-parser"
      ).map(_ % circeVersion)
    }
  }

  val infraDependencies: Seq[ModuleID] = {
    Seq(
      //grpc
      "io.grpc" % "grpc-netty-shaded" % scalapb.compiler.Version.grpcJavaVersion,
      //kafka
//      "com.banno" %% "kafka4s" % "8.0.3",
      "com.github.fd4s" %% "fs2-kafka" % "3.9.1",
//      "com.github.fd4s" %% "fs2-kafka-circe" % "3.9.1",
//      "com.github.fd4s" %% "fs2-kafka-vulcan" % "3.9.1",
      //postgres
//      "org.tpolecat" %% "doobie-core"     % "1.0.0-RC11",
//      "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC11",
//      "org.tpolecat" %% "doobie-hikari"   % "1.0.0-RC11"
      "org.tpolecat" %% "skunk-core" % "0.6.5",
    )
  }

  val logging: Seq[ModuleID] = {
    Seq(
      "org.typelevel" %% "log4cats-core" % "2.7.1",
      "org.typelevel" %% "log4cats-slf4j" % "2.7.1",
      "org.slf4j" % "slf4j-simple" % "2.0.17",
    )
  }

  val dependencies: Seq[ModuleID] = {
    logging ++ infraDependencies ++ util ++ cats ++ testDependencies
  }

}
