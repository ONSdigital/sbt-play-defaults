package uk.gov.ons.sbt

import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport.{MergeStrategy, assembly, assemblyJarName, assemblyMergeStrategy}
import sbtassembly.{AssemblyPlugin, PathList}
import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.PlayImport.{PlayKeys, cache, filters, ws}
import play.sbt.PlayScala
import play.sbt.routes.RoutesKeys.{routesGenerator, routesImport}
import com.typesafe.sbt.packager.universal.UniversalPlugin

object OnsPlayDefaultsPlugin extends AutoPlugin {

  override def trigger = allRequirements
  override def requires = PlayScala && AssemblyPlugin && UniversalPlugin

  override lazy val projectSettings = {
    libraryDependencySettings ++
    playSettings ++
    assemblySettings
  }

  override lazy val projectConfigurations = Seq(ITest)

  lazy val ITest: Configuration = config("it") extend Test

  private[this] val playSettings = Seq(
    routesImport += "extensions.Binders._",
    //moduleName := "sbr-admin-data",
    description := "<description>",
    //    libraryDependencies ++= devDeps,
    // di router -> swagger
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

  lazy val testSettings = Seq(
    sourceDirectory in ITest := baseDirectory.value / "test/controllers/v1/it",
    resourceDirectory in ITest := baseDirectory.value / "test/resources",
    scalaSource in ITest := baseDirectory.value / "test/controllers/v1/it",
    // test setup
    parallelExecution in Test := false
  )

  lazy val assemblySettings = Seq(
    assemblyJarName in assembly := s"${name.value}-assembly-${version.value}.jar",
    assemblyMergeStrategy in assembly := {
      case PathList("org", "apache", xs@_*)                             => MergeStrategy.last
      case PathList("META-INF", "io.netty.versions.properties", xs@_ *) => MergeStrategy.last
      case "application.conf"                                           => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    mainClass in assembly := Some("play.core.server.ProdServerStart"),
    fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)
  )

}
