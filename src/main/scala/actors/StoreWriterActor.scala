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
    case OnChange(sources, destinations) =>
      log.debug("StoreWriteActor.OnChange")

      StoreFileUtil.write(StoreContents(
        sources.map(s => s.name).toList,
        destinations.map(d => d.name).toList
      ))
  }
}
