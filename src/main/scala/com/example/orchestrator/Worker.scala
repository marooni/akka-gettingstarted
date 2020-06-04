package com.example.orchestrator

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, TimerScheduler}
import com.example.akka.intro.iot.Device
import com.example.orchestrator.Worker.{StatusTimeout, WorkerMessage}

import scala.concurrent.duration._

class Worker(url: String,
             context: ActorContext[WorkerMessage],
             timers: TimerScheduler[WorkerMessage])
  extends AbstractBehavior[WorkerMessage] {

  context.log.info(s"Worker started: sending request to $url/request")

  timers.startSingleTimer(StatusTimeout, StatusTimeout, 5.seconds)

  override def onMessage(msg: WorkerMessage): Behavior[WorkerMessage] =
    msg match {
      case StatusTimeout => onStatusTimeout()
    }

  private def onStatusTimeout(): Behavior[WorkerMessage] = {
    context.log.info(s"Request finished $url")
    Behaviors.stopped
  }
}

object Worker {

  def apply(url: String): Behavior[WorkerMessage] = {
    Behaviors.setup { context =>
      Behaviors.withTimers { timers =>
        new Worker(url, context, timers)
      }
    }
  }

  trait WorkerMessage

  private case object StatusTimeout extends WorkerMessage

  final case class WorkerTerminated(worker: ActorRef[WorkerMessage], url: String) extends WorkerMessage
}