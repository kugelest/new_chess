enablePlugins(JavaAppPackaging)
// enablePlugins(JavaServerAppPackaging)
enablePlugins(GatlingPlugin)


Global / excludeLintKeys += name
ThisBuild / name         := "chess_root"
ThisBuild / organization := "htwg.se"
ThisBuild / version      := "0.1"
ThisBuild / scalaVersion := "3.3.3"

lazy val pekkoHttpVersion = "1.0.1"
lazy val pekkoVersion     = "1.0.2"
lazy val sl4jVersion      = "1.7.25"
lazy val playJsonVersion  = "3.0.3"
lazy val gatlingVersion   = "3.11.4"

lazy val commonDependencies = Seq(
  "org.apache.pekko"     %% "pekko-http"                % pekkoHttpVersion,
  "org.apache.pekko"     %% "pekko-http-spray-json"     % pekkoHttpVersion,
  "org.apache.pekko"     %% "pekko-actor-typed"         % pekkoVersion,
  "org.apache.pekko"     %% "pekko-stream"              % pekkoVersion,
  "org.playframework"    %% "play-json"                 % playJsonVersion,
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test,it",
  "io.gatling"            % "gatling-test-framework"    % gatlingVersion % "test,it",
)

libraryDependencies ++= commonDependencies

dockerExposedPorts ++= Seq(8090)
