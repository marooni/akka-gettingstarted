package com.example.akka.intro.iot

import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors
import com.example.akka.intro.iot.DeviceManager.DeviceManagerMessage

import scala.concurrent.duration._

class DeviceGroup(context: ActorContext[DeviceGroup.DeviceGroupMessage], groupId: String)
  extends AbstractBehavior[DeviceGroup.DeviceGroupMessage] {

  import DeviceGroup._
  import DeviceManager._

  private var deviceIdToActor = Map.empty[String, ActorRef[Device.DeviceMessage]]

  context.log.info("DeviceGroup {} started", groupId)

  override def onMessage(msg: DeviceGroupMessage): Behavior[DeviceGroupMessage] =
    msg match {
      case trackMsg @ RequestTrackDevice(`groupId`, deviceId, replyTo) =>
        deviceIdToActor.get(deviceId) match {
          case Some(deviceActor) =>
            replyTo ! DeviceRegistered(deviceActor)
          case None =>
            context.log.info("Creating device actor for {}", trackMsg.deviceId)
            val deviceActor = context.spawn(Device(groupId, deviceId), s"device-$deviceId")
            context.watchWith(deviceActor, DeviceTerminated(deviceActor, groupId, deviceId))
            deviceIdToActor += deviceId -> deviceActor
            replyTo ! DeviceRegistered(deviceActor)
        }
        this

      case RequestTrackDevice(gId, _, _) =>
        context.log.warning("Ignoring TrackDevice request for {}. This actor is responsible for {}.", gId, groupId)
        this

      case RequestDeviceList(requestId, gId, replyTo) =>
        if (gId == groupId) {
          replyTo ! ReplyDeviceList(requestId, deviceIdToActor.keySet)
          this
        } else
          Behaviors.unhandled

      case DeviceTerminated(_, _, deviceId) =>
        context.log.info("Device actor for {} has been terminated", deviceId)
        deviceIdToActor -= deviceId
        this

      case RequestAllTemperatures(requestId, gId, replyTo) =>
        if (gId == groupId) {
          context.spawnAnonymous(
            DeviceGroupQuery(deviceIdToActor, requestId = requestId, requester = replyTo, 3.seconds))
          this
        } else
          Behaviors.unhandled

    }

  override def onSignal: PartialFunction[Signal, Behavior[DeviceGroupMessage]] = {
    case PostStop =>
      context.log.info("DeviceGroup {} stopped", groupId)
      this
  }
}

object DeviceGroup {
  def apply(groupId: String): Behavior[DeviceGroupMessage] =
    Behaviors.setup(context => new DeviceGroup(context, groupId))

  trait DeviceGroupMessage

  private final case class DeviceTerminated(device: ActorRef[Device.DeviceMessage], groupId: String, deviceId: String)
    extends DeviceGroupMessage

  import DeviceGroupQuery.DeviceGroupQueryMessage

  final case class RequestAllTemperatures(requestId: Long, groupId: String, replyTo: ActorRef[RespondAllTemperatures])
    extends DeviceGroupQueryMessage
      with DeviceGroupMessage
      with DeviceManagerMessage

  final case class RespondAllTemperatures(requestId: Long, temperatures: Map[String, TemperatureReading])

  sealed trait TemperatureReading
  final case class Temperature(value: Double) extends TemperatureReading
  case object TemperatureNotAvailable extends TemperatureReading
  case object DeviceNotAvailable extends TemperatureReading
  case object DeviceTimedOut extends TemperatureReading

}