ThisBuild / organization := "htwg.se.new_chess"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "3.3.1"
// ThisBuild / coverageExcludedPackages := "htwg\\.se\\.chess\\.aview\\.gui;.*FileIOComponent.*;.*Chess"

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "org.playframework" %% "play-json" % "3.0.1"

lazy val BoardComponent = project
	.settings(
		// other settings
		libraryDependencies += "com.google.inject" % "guice" % "5.1.0",
		libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.4"
	)


lazy val FileIOComponent = project
	.dependsOn(BoardComponent)
	.settings(
		// libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
		// other settings
	)

lazy val root = (project in file("."))
  .aggregate(BoardComponent, FileIOComponent)
  .dependsOn(BoardComponent, FileIOComponent)
  .settings(
    libraryDependencies += "net.codingwell" %% "scala-guice" % "5.1.1",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "com.typesafe.play" %% "play-test" % "2.9.0-M3" % Test
  )

// name := "chess"
// version := "0.1"
// organization := "htwg.se.chess"
// scalaVersion := "3.2.1"

// libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15"
// libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
// libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
// libraryDependencies += "com.google.inject" % "guice" % "5.1.0"
// libraryDependencies += "net.codingwell" %% "scala-guice" % "5.1.1"
// libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
// libraryDependencies += "com.typesafe.play" %% "play-test" % "2.9.0-M3" % Test

//coverageExcludedPackages := "htwg\\.se\\.chess\\.aview\\.gui;.*FileIOComponent.*;.*Chess"
