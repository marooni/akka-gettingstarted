package com.example.akka

import akka.actor.typed.ActorSystem
import com.example.akka.intro.iot.IotSupervisor
import com.example.akka.quickstart.GreeterMain
import com.example.akka.quickstart.GreeterMain.SayHello
import com.example.implicits._

object Main extends App {

  // Akka Quickstart
  //
  // val greeterMain: ActorSystem[GreeterMain.SayHello] = ActorSystem(GreeterMain(), "AkkaQuickStart")
  // greeterMain ! SayHello("Charles")

  // ActorHierarchy Experiment
  //
  // val testSystem = ActorSystem(ActorHierarchyExperiment(), "testSystem")
  // testSystem ! "start"

  // StartStop Experiment
  //
  // val first = ActorSystem(StartStopActor1(), "first")
  // first ! "stop"

  // Supervising Experiment
  //
  // val supervisingActor = ActorSystem(SupervisingActor(), "supervising-actor")
  // supervisingActor ! "failChild"

  // IoT System
  //
  //val iotSystem = ActorSystem[Nothing](IotSupervisor(), "iot-system")

  // implicit tests
  //
  val connector = DataConnector()
  implicit val dataFetcher = DatabaseDataFetcher()
  println(connector.getData())

}
