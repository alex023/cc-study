import sbt._

object Dependencies {
  val versionScala = "2.12.7"
  val versionScalaLib = "2.12"

  val _scalaXml = ("org.scala-lang.modules" %% "scala-xml" % "1.1.0")
    .exclude("org.scala-lang", "scala-library")

  val _scalaJava8Compat =
    ("org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0")
      .exclude("org.scala-lang", "scala-library")

  val _scalatest = "org.scalatest" %% "scalatest" % "3.0.5"

  val versionAkka = "2.5.17"

  lazy val _akkas = Seq(
    "com.typesafe.akka" %% "akka-slf4j" % versionAkka,
    "com.typesafe.akka" %% "akka-actor" % versionAkka,
    "com.typesafe.akka" %% "akka-stream" % versionAkka,
    "com.typesafe.akka" %% "akka-remote" % versionAkka,
    "com.typesafe.akka" %% "akka-testkit" % versionAkka % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % versionAkka % Test
  ).map(
    _.exclude("org.scala-lang.modules", s"scala-java8-compat")
      .cross(CrossVersion.binary)
  )

  lazy val _akkaPersistence = "com.typesafe.akka" %% "akka-persistence-query" % versionAkka

  lazy val _akkaMultiNodeTestkit = "com.typesafe.akka" %% "akka-multi-node-testkit" % versionAkka % Test

  lazy val _akkaClusters = Seq(
    "com.typesafe.akka" %% "akka-cluster" % versionAkka,
    "com.typesafe.akka" %% "akka-cluster-tools" % versionAkka,
    "com.typesafe.akka" %% "akka-cluster-metrics" % versionAkka,
    "com.typesafe.akka" %% "akka-cluster-sharding" % versionAkka,
    _akkaMultiNodeTestkit
  )

  lazy val _kryoSerialization = Seq("com.twitter" %% "chill-akka" % "0.9.3")

  private val versionAkkaPersistenceCassandra = "0.82"
  val _akkaPersistenceCassandras = Seq(
    "com.typesafe.akka" %% "akka-persistence-cassandra" % versionAkkaPersistenceCassandra,
    "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % versionAkkaPersistenceCassandra % Test
  )

  private val versionSlick = "3.2.3"
  val _slicks = Seq(
    "com.typesafe.slick" %% "slick" % versionSlick,
    ("com.typesafe.slick" %% "slick-hikaricp" % versionSlick)
      .exclude("com.zaxxer", "HikariCP")
  )
  val _slickCodegen = "com.typesafe.slick" %% "slick-codegen" % versionSlick

  val _config = "com.typesafe" % "config" % "1.3.3"

  //  val _shapeless = "com.chuusai" %% "shapeless" % "2.3.3"

  val _scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"

  val _logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
}
