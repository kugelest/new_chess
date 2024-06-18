enablePlugins(JavaAppPackaging)
enablePlugins(JavaServerAppPackaging)

Global / excludeLintKeys += name
ThisBuild / name         := "chess_board"
ThisBuild / organization := "htwg.se"
ThisBuild / version      := "0.1"
ThisBuild / scalaVersion := "3.3.3"

lazy val pekkoHttpVersion = "1.0.1"
lazy val pekkoVersion     = "1.0.2"
lazy val sl4jVersion      = "1.7.25"
lazy val playJsonVersion  = "3.0.3"

lazy val commonDependencies = Seq(
  "org.apache.pekko"  %% "pekko-http"            % pekkoHttpVersion,
  "org.apache.pekko"  %% "pekko-http-spray-json" % pekkoHttpVersion,
  "org.apache.pekko"  %% "pekko-actor-typed"     % pekkoVersion,
  "org.apache.pekko"  %% "pekko-stream"          % pekkoVersion,
  "org.playframework" %% "play-json"             % playJsonVersion,
	"com.typesafe.slick" %% "slick" % "3.5.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.5.1",
  "org.postgresql" % "postgresql" % "42.7.3",
  "com.github.tminglei" %% "slick-pg" % "0.22.2",
  // "com.github.tminglei" %% "slick-pg_spray-json" % "0.22.2",
	"org.slf4j" % "slf4j-nop" % "1.7.26",
)

libraryDependencies ++= commonDependencies

dockerExposedPorts ++= Seq(8081)
