package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import models.{Backup, BackupSource}

import scala.collection.mutable

object StoreActor {

  def props: Props = Props[StoreActor]

  final case class AddBackup(backup: Backup)
  final case class AddBackups(backups: Seq[Backup])
  final case class AddSource(source: BackupSource)
  final case class AddSources(sources: Seq[BackupSource])
  final case class OnChange(backups: Seq[Backup], sources: Seq[BackupSource])
  final case object Subscribe
}

class StoreActor extends Actor with ActorLogging {
  import StoreActor._

  private val backups: mutable.MutableList[Backup] = mutable.MutableList()
  private val backupSources: mutable.MutableList[BackupSource] = mutable.MutableList()
  private val subscribers: mutable.MutableList[ActorRef] = mutable.MutableList()

  context.actorOf(StoreWriterActor.props)

  def receive = {
    case AddBackup(b) =>
      log.debug("StoreActor.AddBackup")
      addBackups(Seq(b))

    case AddBackups(b) =>
      log.debug("StoreActor.AddBackups")
      addBackups(b)

    case AddSource(s) =>
      log.debug("StoreActor.AddSource")
      addSources(Seq(s))

    case AddSources(s) =>
      log.debug("StoreActor.AddSources")
      addSources(s)

    case Subscribe =>
      log.debug(s"StoreActor.Subscribe: ${sender()}")
      subscribers += sender()
      // initialize with current state
      sender() ! OnChange(backups, backupSources)
  }

  private def addBackups(_backups: Seq[Backup]) = {
    backups ++= _backups

    subscribers.foreach(s => s ! OnChange(backups, backupSources))
  }

  private def addSources(sources: Seq[BackupSource]) = {
    backupSources ++= sources

    subscribers.foreach(s => s ! OnChange(backups, backupSources))
  }
}
