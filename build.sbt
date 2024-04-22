Global / excludeLintKeys += name
ThisBuild / name         := "chess"
ThisBuild / organization := "htwg.se"
ThisBuild / version      := "0.1"
ThisBuild / scalaVersion := "3.3.1"

lazy val pekkoHttpVersion = "1.0.1"
lazy val pekkoVersion     = "1.0.2"

libraryDependencies ++= Seq(
  "org.apache.pekko" %% "pekko-http"            % pekkoHttpVersion,
  "org.apache.pekko" %% "pekko-http-spray-json" % pekkoHttpVersion,
  "org.apache.pekko" %% "pekko-actor-typed"     % pekkoVersion,
  "org.apache.pekko" %% "pekko-stream"          % pekkoVersion
)

lazy val BoardComponent = (project in file("BoardComponent"))
  .settings(
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.4"
  )

lazy val FileIOComponent = (project in file("FileIOComponent"))
  .dependsOn(BoardComponent)
  .settings(
  )

lazy val root = (project in file("."))
  .dependsOn(BoardComponent, FileIOComponent)
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
  )
