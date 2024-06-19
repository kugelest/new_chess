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
  // "org.mongodb.scala" %% "mongo-scala-driver" % "5.1.0",
  "org.apache.pekko"  %% "pekko-http"            % pekkoHttpVersion,
  "org.apache.pekko"  %% "pekko-http-spray-json" % pekkoHttpVersion,
  "org.apache.pekko"  %% "pekko-actor-typed"     % pekkoVersion,
  "org.apache.pekko"  %% "pekko-stream"          % pekkoVersion,
  "org.playframework" %% "play-json"             % playJsonVersion,
	"com.typesafe.slick" %% "slick" % "3.5.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.5.1",
  "org.postgresql" % "postgresql" % "42.7.3",
  "com.github.tminglei" %% "slick-pg" % "0.22.2",
  // "org.mongodb" % "mongodb-driver-sync" % "5.1.1",
  // "org.mongodb" % "mongodb-driver" % "3.12.14",
  // "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0",
	"org.slf4j" % "slf4j-nop" % "1.7.26",
)

libraryDependencies += ("org.mongodb.scala" %% "mongo-scala-driver" % "4.3.3").cross(CrossVersion.for3Use2_13)
libraryDependencies ++= commonDependencies

dockerExposedPorts ++= Seq(8081)
