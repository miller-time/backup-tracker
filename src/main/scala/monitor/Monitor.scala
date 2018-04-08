package monitor

import actors.BackupTrackerActors
import actors.StoreActor.{OnChange, Subscribe}
import akka.actor.{Actor, ActorLogging, Props}
import components.{SectionLabel, SelectableLabel}
import models.Backup
import scalafx.application.Platform
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, VBox}

import scala.collection.mutable

object Monitor extends BorderPane {
  val backupsVBox: VBox = new VBox {
    spacing = 2
  }

  class MonitorActor extends Actor with ActorLogging {
    BackupTrackerActors.store ! Subscribe

    def receive = {
      case OnChange(backups, _) =>
        log.debug("MonitorActor.OnChange")
        Platform.runLater {
          backupsVBox.children = buildBackups(backups)
        }
    }
  }

  def apply(): BorderPane = {
    val backupsContainer = new VBox {
      spacing = 4
      children = List(
        SectionLabel("Backups"),
        new Separator,
        backupsVBox
      )
    }

    BackupTrackerActors.system.actorOf(Props(new MonitorActor))

    new BorderPane {
      styleClass = Seq("main")
      center = backupsContainer
    }
  }

  private def buildBackups(backupModels: Seq[Backup]): mutable.MutableList[Node] = {
    if (backupModels.isEmpty)
      mutable.MutableList(SelectableLabel("No backups added"))
    else {
      val backups = backupModels.map(backup => {
        val description = s"${backup.sourcePath} -> ${backup.destinationPath}"
        SelectableLabel(description)
      })
      mutable.MutableList(backups: _*)
    }
  }
}
