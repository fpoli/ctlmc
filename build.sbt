import com.github.retronym.SbtOneJar._

lazy val root = (project in file(".")).
	settings(
		name := "ctl-model-checking",
		version := "0.1",
		scalaVersion := "2.11.5"
	)

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3"

scalacOptions += "-feature"

oneJarSettings
