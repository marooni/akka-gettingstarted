package com.example.akka.quickstart

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

// The Main object creates ActorSystem with a guardian. The guardian is the top level actor
// that bootstraps your application.
// The guardian is typically defined with Behaviors.setup that contains the initial bootstrap.

object GreeterMain {

  final case class SayHello(name: String)

  // The GreeterMain creates a Greeter actor this way on startup as well as a new GreeterBot
  // each time it receives a SayHello message.

  def apply(): Behavior[SayHello] =

    Behaviors.setup { context =>

      // creates a Greeter actor on startup
      val greeter = context.spawn(Greeter(), "greeter")

      Behaviors.receiveMessage { message =>
        // creates a new GreeterBot each time it receives a SayHello message
        val replyTo = context.spawn(GreeterBot(max = 3), message.name)
        greeter ! Greeter.Greet(message.name, replyTo)
        Behaviors.same
      }
    }

}
