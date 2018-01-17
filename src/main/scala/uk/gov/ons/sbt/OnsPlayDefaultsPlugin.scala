package uk.gov.ons.sbt

import sbt.Keys._
import sbt._
import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.PlayImport.{cache, filters, ws}
import play.sbt.PlayScala
import play.sbt.routes.RoutesKeys.{routesGenerator, routesImport}

object OnsPlayDefaultsPlugin extends AutoPlugin {

  override def trigger = allRequirements
  override def requires = PlayScala

  override lazy val projectSettings = {
    libraryDependencySettings ++
    playSettings ++
    testSettings
  }

  override lazy val projectConfigurations = Seq(ITest)

  lazy val ITest: Configuration = config("it") extend Test

  private[this] val playSettings = Seq(
    description := "<description>",
    routesGenerator := InjectedRoutesGenerator,
    dependencyOverrides += "com.google.guava" % "guava" % "14.0.1",
    unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")
  )

  private[this] val libraryDependencySettings = Seq(
    libraryDependencies ++= dependencySettings
  )

  private[this] val dependencySettings = Seq(
    cache,
    ws,
    filters,
    "org.scalactic"              %%  "scalactic"       %   "3.0.4",
    "org.webjars"                %%  "webjars-play"    %   "2.5.0-3",
    "io.swagger"                 %%  "swagger-play2"   %   "1.5.3",
    "org.webjars"                %   "swagger-ui"      %   "2.2.10-1",
    // Metrics
    "io.dropwizard.metrics"      %   "metrics-core"    %   "3.2.5",
    // Controller
    "io.swagger"                 %%  "swagger-play2"   %   "1.5.1",
    "com.typesafe.scala-logging" %%  "scala-logging"   %   "3.7.2",
    "ch.qos.logback"             %   "logback-classic" %   "1.2.3",
    "org.scalatestplus.play"     %%  "scalatestplus-play" % "2.0.0" % "test"
  )

  private[this] lazy val testSettings = Seq(
    sourceDirectory in ITest := baseDirectory.value / "test/controllers/v1/it",
    resourceDirectory in ITest := baseDirectory.value / "test/resources",
    scalaSource in ITest := baseDirectory.value / "test/controllers/v1/it",
    // test setup
    parallelExecution in Test := false
  )

}
