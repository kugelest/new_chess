ThisBuild / organization := "htwg.se.chess"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "3.2.1"
// ThisBuild / coverageExcludedPackages := "htwg\\.se\\.chess\\.aview\\.gui;.*FileIOComponent.*;.*Chess"

val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.5.2"
// libraryDependencies ++= Seq(
//   "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
//   "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
//   "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
// )

ThisBuild / assemblyMergeStrategy := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "StaticLoggerBinder.class" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "StaticMDCBinder.class" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "StaticMarkerBinder.class" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "io.netty.versions.properties" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "BUILD" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "module-info.class"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

lazy val BoardComponent =
  (project in file("BoardComponent"))
    .settings(
      // other settings
      // libraryDependencies += "com.google.inject" % "guice" % "5.1.0"
      libraryDependencies += "com.google.inject" % "guice" % "7.0.0",
      libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
      libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC7",
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
        "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
        "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
      )
    )

lazy val FileIOComponent =
  (project in file("FileIOComponent"))
    .dependsOn(BoardComponent)
    .settings(
      libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC7",
      libraryDependencies += "com.google.inject" % "guice" % "7.0.0",
      libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0"
      // libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
      // other settings
    )

lazy val root = (project in file("."))
  .aggregate(BoardComponent, FileIOComponent)
  .dependsOn(BoardComponent, FileIOComponent)
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "com.google.inject" % "guice" % "7.0.0",
    libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
    )
  )
