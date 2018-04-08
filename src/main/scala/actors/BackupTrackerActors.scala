package actors

import akka.actor.ActorSystem

object BackupTrackerActors {
  println("initializing actor system")
  val system = ActorSystem("backupTracker")

  val store = system.actorOf(StoreActor.props, "store")
}
