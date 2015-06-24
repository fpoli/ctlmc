resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")
addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")

resolvers += "Linter Repository" at "https://hairyfotr.github.io/linteRepo/releases"
addCompilerPlugin("com.foursquare.lint" %% "linter" % "0.1.11")
