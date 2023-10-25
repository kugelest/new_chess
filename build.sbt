ThisBuild / organization := "htwg.se.chess"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "3.2.1"

// ThisBuild / assemblyMergeStrategy := {
//   case x => MergeStrategy.first
// }

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
      libraryDependencies += "com.google.inject" % "guice" % "7.0.0",
      libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
    )

lazy val root = (project in file("."))
  .aggregate(BoardComponent)
  .dependsOn(BoardComponent)
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "com.google.inject" % "guice" % "7.0.0",
    libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
  )
