package actors

import actors.StoreActor.{OnChange, Subscribe}
import akka.actor.{Actor, ActorLogging, Props}
import models.StoreFileUtil
import models.StoreFileUtil.StoreContents

object StoreWriterActor {
  def props = Props[StoreWriterActor]
}

class StoreWriterActor extends Actor with ActorLogging {
  BackupTrackerActors.store ! Subscribe

  def receive = {
    case OnChange(backups, sources) =>
      log.debug("StoreWriteActor.OnChange")

      StoreFileUtil.write(StoreContents(
        backups.map(b => (b.sourcePath, b.destinationPath)).toList,
        sources.map(s => s.path).toList
      ))
  }
}
