ThisBuild / scalaVersion     := "3.3.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.d3javu"
ThisBuild / organizationName := "d3javu"

lazy val root = (project in file("."))
  .settings(
    name := "emailConfirmationService",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.11",
      "dev.zio" %% "zio-test" % "2.1.11" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

libraryDependencies += "dev.zio" %% "zio-kafka" % "2.7.0"
libraryDependencies += "com.google.guava" % "guava" % "33.4.8-jre"
libraryDependencies ++= Seq(
"io.github.zeal18" %% "zio-mongodb-bson"    % "0.11.1",
"io.github.zeal18" %% "zio-mongodb-driver"  % "0.11.1",
"io.github.zeal18" %% "zio-mongodb-testkit" % "0.11.1" % Test
)
libraryDependencies += "com.github.eikek" %% "emil-common" % "0.19.0"
libraryDependencies += "com.github.eikek" %% "emil-javamail" % "0.19.0"
libraryDependencies += "dev.zio" %% "zio-http" % "3.2.0"
libraryDependencies += "dev.zio" %% "zio-json" % "0.7.43"