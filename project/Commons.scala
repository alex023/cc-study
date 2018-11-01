import sbt.Keys._
import sbt._

object Commons {

  def basicSettings =
    Version.versionning ++ Seq(
      organization := "pcsir.net",
      organizationName := "pcsir",
      organizationHomepage := Some(url("http://www.pcsir.net")),
      homepage := Some(url("http://www.pcsir.net")),
      startYear := Some(2018),
      scalaVersion := Dependencies.versionScala,
      scalacOptions ++= {
        val opts = Seq(
          "-encoding",
          "UTF-8", // yes, this is 2 args
          "-feature",
          "-deprecation",
          "-unchecked",
          "-Xlint",
          "-Xlog-reflective-calls",
          "-Yno-adapted-args", //akka-http heavily depends on adapted args and => Unit implicits break otherwise
          "-Ywarn-dead-code"
          // "-Xfuture" // breaks => Unit implicits
        )
        Environment.buildEnv.value match {
          case env @ Environment.BuildEnv.Production =>
            opts ++ List("-Xelide-below", "OFF")
          case env => opts
        }
      },
      javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
      javaOptions in run ++= Seq(
        "-Xms128m",
        "-Xmx1024m",
        "-Djava.library.path=./target/native"
      ),
      shellPrompt := { s => Project.extract(s).currentProject.id + " > "
      },
      fork in run := true,
      fork in Test := true,
      parallelExecution in Test := false
    ) ++ Environment.settings // ++ Formatting.settings

}

object Publishing {

  import Environment._

  //  lazy val publishing = Seq(
  //    publishTo := (if (buildEnv.value == BuildEnv.Developement) {
  //                    Some(
  //                      "hualongdata-sbt-dev-local" at "https://artifactory.hualongdata.com/artifactory/sbt-dev-local;build.timestamp=" + new java.util.Date().getTime)
  //                  } else {
  //                    Some(
  //                      "hualongdata-sbt-release-local" at "https://artifactory.hualongdata.com/artifactory/sbt-release-local")
  //                  }),
  //    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials_yangbajing")
  //  )

  lazy val noPublish =
    Seq(publish := ((): Unit), publishLocal := ((): Unit), publishTo := None)
}

object Environment {

  object BuildEnv extends Enumeration {
    val Production, Stage, Test, Developement = Value
  }

  lazy val buildEnv =
    settingKey[BuildEnv.Value]("The current build environment")

  val settings =
    Seq(buildEnv := {
      sys.props
        .get("build.env")
        .orElse(sys.env.get("BUILD_ENV"))
        .flatMap {
          case "prod"  => Some(BuildEnv.Production)
          case "stage" => Some(BuildEnv.Stage)
          case "test"  => Some(BuildEnv.Test)
          case "dev"   => Some(BuildEnv.Developement)
          case _       => None
        }
        .getOrElse(BuildEnv.Developement)
    }, onLoadMessage := {
      // old message as well
      val defaultMessage = onLoadMessage.value
      val env = buildEnv.value
      s"""|$defaultMessage
          |Working in build environment: $env""".stripMargin
    })
}


