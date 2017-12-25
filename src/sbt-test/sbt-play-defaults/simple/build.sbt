import play.sbt.PlayScala

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.11"
  )
