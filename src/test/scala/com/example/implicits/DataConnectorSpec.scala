package com.example.implicits

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.WordSpecLike

class DataConnectorSpec extends ScalaTestWithActorTestKit with WordSpecLike {

  "A DataConnector" must {
    "reply with a string" in {
      val underTest = DataConnector()
      implicit val dataFetcher = MockedDataFetcher()
      val result = underTest.getData()
      println(result)
    }
  }
}
