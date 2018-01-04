
organization := "uk.gov.ons"
name := """sbt-play-defaults"""
version := "0.1-SNAPSHOT"

// SBT 0.13 requires 2.10
// See https://github.com/allenai/sbt-plugins/blob/master/build.sbt
scalaVersion := "2.10.7"

sbtPlugin := true

resolvers += Resolver.sonatypeRepo("releases")

initialCommands in console := """import uk.gov.ons.sbt._"""

// set up 'scripted; sbt plugin for testing sbt plugins
scriptedSettings
scriptedLaunchOpts := {
  scriptedLaunchOpts.value ++ Seq(
    "-Xmx1024M", "-Dplugin.version=" + version.value
  )
}
scriptedBufferLog := false

test.in(Test) := {
  test.in(Test).dependsOn(scripted.toTask("")).value
}

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.18")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")
