ThisBuild / scalaVersion := "3.5.2"
ThisBuild / organization := "com.skycoin4444"
ThisBuild / version := "1.0.0"

lazy val root = (project in file("."))
  .settings(
    name := "sky-pdf-core",
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.2" % Test
  )
