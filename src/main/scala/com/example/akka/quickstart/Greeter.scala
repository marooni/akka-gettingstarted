package com.example.akka.quickstart

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

/*
The example consists of three actors:

Greeter: Receives commands to Greet someone and responds with a Greeted to confirm the greeting has taken place
GreeterBot: receives the reply from the Greeter and sends a number of additional greeting messages and collect the replies until a given max number of messages have been reached.
GreeterMain: The guardian actor that bootstraps everything
*/

// (1) The accepted message types of an Actor together with all reply types defines the protocol
//     spoken by this Actor;  in this case it is a simple request–reply protocol.
// (2) The protocol is bundled together with the behavior that implements it in
//     a nicely wrapped scope: the Greeter object.

object Greeter {

  // command sent to the Greeter actor to greet
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])

  // reply from the Greeter actor to confirm the greeting has happened
  final case class Greeted(whom: String, from: ActorRef[Greet])

  // It is a good practice obtain an actor’s initial behavior in the object’s apply method
  // (1) The behavior of the Actor is defined as the Greeter with the help of the Behaviors.receive behavior factory
  // (2) The type of the messages handled by this behavior is declared to be of class Greet, meaning that the
  //     message argument is also typed as such.

  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>

    // (1) We can access the whom and replyTo members without needing to use a pattern match
    // (2) The Greeter Actor send a message to another Actor, which is done using the ! operator.
    //     It is an asynchronous operation that doesn’t block the caller’s thread.
    // (3) Since the replyTo address is declared to be of type ActorRef[Greeted], the compiler will only
    //     permit us to send messages of this type, other usage will be a compiler error.

    context.log.info("Hello {}!", message.whom)
    message.replyTo ! Greeted(message.whom, context.self)

    // (1) State is updated by returning a new behavior that holds the new immutable state.
    // (2) In this case we don’t need to update any state, so we return Behaviors.same, which means the
    //     next behavior is “the same as the current one”.

    Behaviors.same
  }
}
