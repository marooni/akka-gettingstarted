package com.example.akka.intro.iot

import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors

class DeviceManager(context: ActorContext[DeviceManager.DeviceManagerMessage])
  extends AbstractBehavior[DeviceManager.DeviceManagerMessage] {

  import DeviceManager._
  import DeviceGroup.DeviceGroupMessage

  var groupIdToActor = Map.empty[String, ActorRef[DeviceGroupMessage]]

  context.log.info("DeviceManager started")

  override def onMessage(msg: DeviceManagerMessage): Behavior[DeviceManagerMessage] =
    msg match {
      case trackMsg @ RequestTrackDevice(groupId, _, replyTo) =>
        groupIdToActor.get(groupId) match {
          case Some(ref) =>
            ref ! trackMsg
          case None =>
            context.log.info("Creating device group actor for {}", groupId)
            val groupActor = context.spawn(DeviceGroup(groupId), "group-" + groupId)
            // register for a termination notification with a custom message
            context.watchWith(groupActor, DeviceGroupTerminated(groupId))
            groupActor ! trackMsg
            groupIdToActor += groupId -> groupActor
        }
        this

      case req @ RequestDeviceList(requestId, groupId, replyTo) =>
        groupIdToActor.get(groupId) match {
          case Some(ref) =>
            ref ! req
          case None =>
            replyTo ! ReplyDeviceList(requestId, Set.empty)
        }
        this

      case DeviceGroupTerminated(groupId) =>
        context.log.info("Device group actor for {} has been terminated", groupId)
        groupIdToActor -= groupId
        this
    }

  override def onSignal: PartialFunction[Signal, Behavior[DeviceManagerMessage]] = {
    case PostStop =>
      context.log.info("DeviceManager stopped")
      this
  }

}

object DeviceManager {
  def apply(): Behavior[DeviceManagerMessage] =
    Behaviors.setup(context => new DeviceManager(context))

  import DeviceGroup.DeviceGroupMessage

  trait DeviceManagerMessage

  // message is also send to DeviceGroup therefore it is a DeviceGroupMessage as well
  final case class RequestTrackDevice(groupId: String, deviceId: String, replyTo: ActorRef[DeviceRegistered])
    extends DeviceManagerMessage
      with DeviceGroupMessage

  // message is also send to DeviceGroup therefore it is a DeviceGroupMessage as well
  final case class RequestDeviceList(requestId: Long, groupId: String, replyTo: ActorRef[ReplyDeviceList])
    extends DeviceManagerMessage
      with DeviceGroupMessage

  private final case class DeviceGroupTerminated(groupId: String) extends DeviceManagerMessage

  // these two messages are no DeviceManagerMessage because their are send to / received by an external tbd instance e.g. User
  final case class DeviceRegistered(device: ActorRef[Device.DeviceMessage])
  final case class ReplyDeviceList(requestId: Long, ids: Set[String])
}
