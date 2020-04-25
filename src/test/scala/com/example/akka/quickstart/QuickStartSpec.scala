package com.example.akka.quickstart

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.example.akka.quickstart.Greeter.{Greet, Greeted}
import org.scalatest.WordSpecLike

class QuickStartSpec extends ScalaTestWithActorTestKit with WordSpecLike {

  "A Greeter" must {
    "reply to greeted" in {
      val replyProbe = createTestProbe[Greeted]()
      val underTest = spawn(Greeter())
      underTest ! Greet("Santa", replyProbe.ref)
      replyProbe.expectMessage(Greeted("Santa", underTest.ref))
    }
  }

  "A GreetBot" must {
    "reply to greet" in {
      val replyProbe = createTestProbe[Greet]()
      val underTest = spawn(GreeterBot(max = 3))
      underTest ! Greeted("Santa", replyProbe.ref)
      replyProbe.expectMessage(Greet("Santa", underTest.ref))
    }
  }

}
