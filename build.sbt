ThisBuild / organization := "htwg.se.chess"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "3.2.1"
// ThisBuild / coverageExcludedPackages := "htwg\\.se\\.chess\\.aview\\.gui;.*FileIOComponent.*;.*Chess"

lazy val BoardComponent =
  (project in file("BoardComponent"))
    .settings(
      // other settings
      // libraryDependencies += "com.google.inject" % "guice" % "5.1.0"
      libraryDependencies += "com.google.inject" % "guice" % "7.0.0",
      libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
      libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC7"
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
    libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0"
    // libraryDependencies += "net.codingwell" %% "scala-guice" % "5.1.1",
    // libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    // libraryDependencies += "com.typesafe.play" %% "play-test" % "2.9.0-M3" % Test
  )
