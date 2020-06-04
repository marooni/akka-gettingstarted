package com.example.json

import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Behavior, PostStop, Signal}
import akka.stream.scaladsl.{Keep, Sink}
import akka.stream.typed.scaladsl.ActorMaterializer
import com.example.json.FileIngestor.{FileIngestorMessage, IngestionMessage}

import scala.concurrent.Await
//import slick.dbio._

import scala.concurrent.duration.Duration

class FileIngestor(context: ActorContext[FileIngestorMessage]) extends
  AbstractBehavior[FileIngestorMessage] {

  context.log.info("FileIngestor started")

  //implicit val postgresSession = SlickSession.forConfig("local-postgres")
  //context.system.registerOnTermination(() => postgresSession.close())
  //import postgresSession.profile.api._

  override def onMessage(msg: FileIngestorMessage): Behavior[FileIngestorMessage] = {
    msg match {
      case IngestionMessage(basePath) =>
        context.log.info("Received IngestionMessage {}", basePath)
        ingest(basePath)
        this
    }
  }

  override def onSignal: PartialFunction[Signal, Behavior[FileIngestorMessage]] = {
    case PostStop =>
      context.log.info("FileIngestor stopped")
      this
  }

  def ingest(basePath: String) = {

    implicit val materializer: ActorMaterializer = ActorMaterializer()(context.system)

    context.log.info("Starting ingestion")
    /*
    val source = Sources.telemetryParameterAsJson(basePath)
    val ingestedCountF = source
      .grouped(1000)
      .map { parameterFile =>
        DBIO.sequence(parameterFile.map(buildJSONBSqlu)).transactionally
      }
      .via(Slick.flowWithPassThrough(25, a => a))
      .toMat(Sink.fold(0) {
        case (acc, insertedRows) => acc + insertedRows.sum
      })(Keep.right)*/

    val source = Sources.telemetryParameter2(basePath)
    source.runForeach(parameter => println(parameter.name))

//    source
//      .log("TelemetryParameter", t => t.name)
//      .map { parameterFile =>
//        1
//      }
//      .toMat(Sink.fold(0) {
//        case (acc, insertedRows) => acc + insertedRows
//      })(Keep.right)

//    implicit val executionContext = untypedSystem.dispatchers.defaultGlobalDispatcher
//    Await.ready(done, Duration.Inf).onComplete {
//      case _ => context.system.terminate()
//    }

/*    val t0 = System.currentTimeMillis()
    val done = ingestedCountF.run()

    //implicit val executionContext: ExecutionContextExecutor = context.system.dispatchers.lookup(DispatcherSelector.default())
    implicit val executionContext = untypedSystem.dispatchers.defaultGlobalDispatcher
    //done.onComplete {
    Await.ready(done, Duration.Inf).onComplete {
      case Success(rows) => {
        val t1 = System.currentTimeMillis()
        val duration = Duration(t1 - t0, MILLISECONDS)
        context.log.info("Elapsed time: " + durationPrettyPrint(duration) + " (" + duration.toString() + ")")
        context.log.info(s"Ingested $rows rows")
        context.system.terminate()
      }
      case Failure(t) => {
        context.log.info("An error has occurred: " + t.getMessage)
        context.system.terminate()
      }
    }*/
  }

/*  def buildJSONBSqlu(pf: ParameterFile): DBIO[Int] = {

    //val json = pf.toJson.compactPrint.substring(0,100)
    //context.log.info(pf.name)
    sqlu"""INSERT INTO telemetry (starttime, parameter, data) VALUES ('#${pf.starttime}', '#${pf.name}', '#${pf.toJson.compactPrint}')"""
  }*/

  private def durationPrettyPrint(duration: Duration): String = {
    val hours = duration.toHours
    val minutes = duration.toMinutes
    val seconds = duration.toSeconds
    hours + "h " + (minutes - hours*60) + "min " + (seconds - minutes*60) + "sec"
  }
}

object FileIngestor {

  def apply(): Behavior[FileIngestorMessage] =
    Behaviors.setup(context => new FileIngestor(context))

  trait FileIngestorMessage

  final case class IngestionMessage(basepath:String) extends FileIngestorMessage
}