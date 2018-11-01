import Commons._
import Dependencies._
import Environment._

lazy val root = Project(id = "cc-study", base = file("."))
  .aggregate(`cc-client`, `cc-frontserver`, `cc-backend`, common)
  .settings(settings: _*)

lazy val `cc-client` = project
  .in(file("cc-client"))
  .dependsOn(common % "compile->compile;test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(basicSettings: _*)
  .settings(mainClass in Compile := Some("cc.client.ClientBoot"))

lazy val `cc-frontserver` = project
  .in(file("cc-frontserver"))
  .dependsOn(common % "compile->compile;test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(basicSettings: _*)
  .settings(mainClass in Compile := Some("cc.front.FrontBoot"))

lazy val `cc-backend` = project
  .in(file("cc-backend"))
  .dependsOn(common % "compile->compile;test->test")
  .settings(basicSettings: _*)
  .settings(mainClass in Compile := Some("cc.backend.BackendBoot"))

lazy val common = project
  .in(file("common"))
  .settings(basicSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      _config,
      _logbackClassic,
      _scalaLogging,
      _scalatest % Test
    ) ++ _akkas ++ _slicks ++ _kryoSerialization //++ _akkaClusters
  )
