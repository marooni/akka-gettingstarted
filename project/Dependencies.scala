import sbt._
import Keys._

object Dependencies {
  object Versions {

    val akka: String = "2.5.25"
    val alpakka: String = "1.1.2"
    val scalaTest: String = "3.0.5"
    val logging: String = "3.9.2"
    val logback: String = "1.2.3"
    val fastparse: String = "2.2.2"
  }

  /**
    * FastParse
    */
  val fastparse: ModuleID = "com.lihaoyi" %% "fastparse" % Versions.fastparse

  /**
    * Reactive streams on top of Akka actors.
    */
  val `akka-stream-typed`: ModuleID = "com.typesafe.akka" %% "akka-stream-typed" % Versions.akka

  /**
    * Reactive streams on top of Akka actors.
    */
  val `akka-stream`: ModuleID = "com.typesafe.akka" %% "akka-stream" % Versions.akka

  /**
    * Typed actors.
    */
  val `akka-typed`: ModuleID = "com.typesafe.akka" %% "akka-actor-typed" % Versions.akka

  /**
    * Alpakka
    */    
  val `alpakka`: ModuleID = "com.lightbend.akka" %% "akka-stream-alpakka-file" % Versions.alpakka

  /**
    * Framework for json deserialization/serialization
    */
  val `spray-json`: ModuleID = "io.spray" %% "spray-json" % "1.3.4"

  /**
    * For streaming to/for postgres.
    */
  val slick = "com.lightbend.akka" %% "akka-stream-alpakka-slick" % Versions.alpakka

  val `postgresqlDriver`: ModuleID = "org.postgresql" % "postgresql" % "42.2.8"

  /**
    * Testing framework
    */
  val scalatest: ModuleID = "org.scalatest" %% "scalatest" % Versions.scalaTest

  /**
    * Testkit for typed actors.
    */
  val `akka-testkit-typed`: ModuleID = "com.typesafe.akka" %% "akka-actor-testkit-typed" % Versions.akka

  /**
    * Scala logging
    */
  val logging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % Versions.logging

  /**
    * Dependency for custom logging patterns
    */
  val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

}
