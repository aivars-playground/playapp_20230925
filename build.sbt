name := """playdemo"""
organization := "com.example"
version := "0.1-SNAPSHOT"
versionScheme := Some("semver-spec")

scalaVersion := "3.3.1"

libraryDependencies += guice
libraryDependencies += "org.mongodb.scala" % "mongo-scala-driver_2.13" % "4.10.2"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "6.0.0-RC2" % Test

lazy val root = (project in file(".")).enablePlugins(PlayScala)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
