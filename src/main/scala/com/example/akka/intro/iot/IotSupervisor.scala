package com.example.akka.intro.iot

import akka.actor.typed.Behavior
import akka.actor.typed.PostStop
import akka.actor.typed.Signal
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors

class IotSupervisor(context: ActorContext[Nothing]) extends AbstractBehavior[Nothing] {
  context.log.info("IoT Application started")

  override def onMessage(msg: Nothing): Behavior[Nothing] = {
    // No need to handle any messages
    Behaviors.unhandled
  }

  override def onSignal: PartialFunction[Signal, Behavior[Nothing]] = {
    case PostStop =>
      context.log.info("IoT Application stopped")
      this
  }
}

object IotSupervisor {
  def apply(): Behavior[Nothing] =
    Behaviors.setup[Nothing](context => new IotSupervisor(context))
}
