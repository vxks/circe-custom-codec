ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.14"

lazy val root = (project in file("."))
  .settings(
    name := "yaml-parser",
    libraryDependencies := Seq(
      "io.circe" %% "circe-generic" % "0.14.2",
      "io.circe" %% "circe-yaml" % "0.14.2",
      "org.json4s" %% "json4s-native" % "4.0.6",
    )
  )