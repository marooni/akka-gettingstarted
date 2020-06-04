package com.example.akka

import akka.actor.typed.ActorSystem
import akka.stream.ActorMaterializer
import com.example.akka.intro.experiments.ActorHierarchyExperiment
import com.example.akka.intro.iot.IotSupervisor
import com.example.akka.quickstart.GreeterMain
import com.example.akka.quickstart.GreeterMain.SayHello
import com.example.akka.streams.StreamSetup
import com.example.implicits._
import com.example.json.{FileIngestor, Sources}
import com.example.json.FileIngestor.{FileIngestorMessage, IngestionMessage}
import com.example.orchestrator.Manager
import com.example.orchestrator.Worker.WorkerMessage

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
  // val connector = DataConnector()
  // implicit val dataFetcher = DatabaseDataFetcher()
  // println(connector.getData())

  // Orchestrator
  //
  //val urls = List("a.example.com","b.example.com","c.example.com")
  //val orchestrator = ActorSystem[WorkerMessage](Manager(urls), "orchestration-system")

  // Timescale Ingestion
  //
  //val fileIngestor = ActorSystem[FileIngestorMessage](FileIngestor(), "ingestion-system")
  //val basePath = "/home/mario/data/TD1_Offline/2018/CX_24h/TD1_CX_24h_18001/TD1_CX_15001A_18001"
  //fileIngestor ! IngestionMessage(basePath)

  // Stream Experiments
  StreamSetup()


}
