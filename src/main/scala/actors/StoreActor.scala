package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import models.{BackupDestination, BackupSource}

import scala.collection.mutable

object StoreActor {

  def props: Props = Props[StoreActor]

  final case class AddSource(source: BackupSource)
  final case class AddSources(sources: Seq[BackupSource])
  final case class AddDestination(destination: BackupDestination)
  final case class AddDestinations(destinations: Seq[BackupDestination])
  final case class OnChange(sources: Seq[BackupSource], destinations: Seq[BackupDestination])
  final case object Subscribe
}

class StoreActor extends Actor with ActorLogging {
  import StoreActor._

  private val backupSources: mutable.MutableList[BackupSource] = mutable.MutableList()
  private val backupDestinations: mutable.MutableList[BackupDestination] = mutable.MutableList()
  private val subscribers: mutable.MutableList[ActorRef] = mutable.MutableList()

  context.actorOf(StoreWriterActor.props)

  def receive = {
    case AddSource(s) =>
      log.debug("StoreActor.AddSource")
      addSources(Seq(s))

    case AddSources(s) =>
      log.debug("StoreActor.AddSources")
      addSources(s)

    case AddDestination(d) =>
      log.debug("StoreActor.AddDestination")
      addDestinations(Seq(d))

    case AddDestinations(d) =>
      log.debug("StoreActor.AddDestinations")
      addDestinations(d)

    case Subscribe =>
      log.debug(s"StoreActor.Subscribe: ${sender()}")
      subscribers += sender()
      // initialize with current state
      sender() ! OnChange(backupSources, backupDestinations)
  }

  private def addSources(sources: Seq[BackupSource]) = {
    backupSources ++= sources

    subscribers.foreach(s => s ! OnChange(backupSources, backupDestinations))
  }

  private def addDestinations(destinations: Seq[BackupDestination]) = {
    backupDestinations ++= destinations

    subscribers.foreach(s => s ! OnChange(backupSources, backupDestinations))
  }
}
