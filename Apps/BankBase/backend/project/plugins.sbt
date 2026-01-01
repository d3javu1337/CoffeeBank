addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.6")

libraryDependencies +=
  "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen" % "0.6.0-rc6"

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9")