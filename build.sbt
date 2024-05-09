Global / excludeLintKeys += name
ThisBuild / name         := "chess"
ThisBuild / organization := "htwg.se"
ThisBuild / version      := "0.1"
ThisBuild / scalaVersion := "3.3.3"

lazy val pekkoHttpVersion = "1.0.1"
lazy val pekkoVersion     = "1.0.2"
lazy val sl4jVersion      = "1.7.25"
lazy val playJsonVersion  = "3.0.3"

lazy val commonDependencies = Seq(
  "org.apache.pekko" %% "pekko-http"            % pekkoHttpVersion,
  "org.apache.pekko" %% "pekko-http-spray-json" % pekkoHttpVersion,
  "org.apache.pekko" %% "pekko-actor-typed"     % pekkoVersion,
  "org.apache.pekko" %% "pekko-stream"          % pekkoVersion,
  "org.slf4j"         % "slf4j-api"             % sl4jVersion
)

lazy val root = (project in file("."))
  .dependsOn(BoardComponent, FileIOComponent)
  .aggregate(BoardComponent, FileIOComponent)
  .settings(
    libraryDependencies ++= commonDependencies,
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
  )

lazy val FileIOComponent = (project in file("FileIOComponent"))
  .dependsOn(BoardComponent)
  .aggregate(BoardComponent)
  .settings(
    libraryDependencies ++= commonDependencies,
    libraryDependencies += "org.apache.pekko" %% "pekko-http"            % pekkoHttpVersion,
    libraryDependencies += "org.apache.pekko" %% "pekko-http-spray-json" % pekkoHttpVersion,
    libraryDependencies += "org.apache.pekko" %% "pekko-actor-typed"     % pekkoVersion,
    libraryDependencies += "org.apache.pekko" %% "pekko-stream"          % pekkoVersion
  )

lazy val BoardComponent = (project in file("BoardComponent"))
  .settings(
    libraryDependencies ++= commonDependencies,
    libraryDependencies += "org.playframework" %% "play-json" % playJsonVersion
  )
