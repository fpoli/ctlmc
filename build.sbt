import com.github.retronym.SbtOneJar._

lazy val root = (project in file(".")).
	settings(
		name := "ctl-model-checking",
		version := "0.2.0",
		scalaVersion := "2.11.6"
	)

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3"

scalacOptions += "-optimise"
scalacOptions += "-deprecation"

oneJarSettings
