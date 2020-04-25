package com.example.akka.intro.iot

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.example.akka.intro.iot.DeviceManager.{DeviceRegistered, ReplyDeviceList, RequestDeviceList, RequestTrackDevice}
import org.scalatest.WordSpecLike

class DeviceManagerSpec extends ScalaTestWithActorTestKit with WordSpecLike {

  "be able to register a device actor" in {
    val probe = createTestProbe[DeviceRegistered]()
    val managerActor = spawn(DeviceManager())

    managerActor ! RequestTrackDevice("my-group", "device1", probe.ref)
    val deviceRegisteredMsg = probe.receiveMessage()
    deviceRegisteredMsg.device should !==(None)
  }

  "be able to request device list from a group which does not exist" in {
    val probe = createTestProbe[ReplyDeviceList]()
    val managerActor = spawn(DeviceManager())

    managerActor ! RequestDeviceList(requestId = 1, "my-group", probe.ref)
    val replyMsg = probe.receiveMessage()
    replyMsg.requestId should ===(1)
    replyMsg.ids should ===(Set.empty)
  }

  "be able to request device list from a group which does exist" in {
    val probe1 = createTestProbe[DeviceRegistered]()
    val probe2 = createTestProbe[ReplyDeviceList]()
    val managerActor = spawn(DeviceManager())

    // create a group
    val groupId = "my-group"
    val deviceId = "device1"
    managerActor ! RequestTrackDevice(groupId, deviceId, probe1.ref)
    managerActor ! RequestDeviceList(requestId = 1, groupId, probe2.ref)
    val replyMsg = probe2.receiveMessage()
    replyMsg.requestId should ===(1)
    replyMsg.ids should ===(Set(deviceId))
  }
}
