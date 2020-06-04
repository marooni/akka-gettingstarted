package com.example.orchestrator

import akka.actor.typed.{Behavior, PostStop, Signal}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.example.akka.intro.iot.DeviceGroupQuery.DeviceTerminated
import com.example.orchestrator.Worker.{WorkerMessage, WorkerTerminated}

class Manager(list: List[String],
              context: ActorContext[WorkerMessage])
  extends AbstractBehavior[WorkerMessage]  {

  context.log.info("Manager started")

  list.foreach { url =>
    val worker = context.spawn(Worker(url), s"worker-$url")
    context.watchWith(worker, WorkerTerminated(worker, url))
  }

  override def onMessage(msg: WorkerMessage): Behavior[WorkerMessage] = {
    msg match {
      case WorkerTerminated(_, url) =>
        context.log.info("Worker for endpoint {} has been terminated", url)
        this
    }
  }
}

object Manager {

  def apply(list: List[String]): Behavior[WorkerMessage] =
    Behaviors.setup[WorkerMessage](context => new Manager(list, context))
}