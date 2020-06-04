package com.example.akka.streams

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Graph}
import akka.stream.scaladsl.Source

import scala.concurrent.Future


object StreamSetup {

  def apply(): Unit = {

    import Sources._

    StreamRunner.run(
        //.fizzBuzz("Fizz","Buzz")
        //.factors()
        primeNumbers()
    )
  }
}

object StreamRunner {

  implicit val system = ActorSystem("Stream-Experiments")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  def run[T](stream: Source[T,NotUsed]): Unit = {

    // run the stream
    val done: Future[Done] = stream.runForeach(i => println(i))(materializer)

    // handle the result
    handleResult(done)
  }

  private def handleResult(done: Future[Done]): Unit = {
    done.onComplete(_ => system.terminate())
  }
}
