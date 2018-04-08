package backups

import actors.BackupTrackerActors
import actors.StoreActor.{AddBackup, OnChange, Subscribe}
import akka.actor.{Actor, ActorLogging, Props}
import components.SelectableLabel
import models.Backup
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, HBox, VBox}

import scala.collection.mutable

object Backups extends BorderPane {
  val backupsVBox: VBox = new VBox {
    spacing = 2
  }
  val sourceItems = new ObservableBuffer[String]()

  class BackupsActor extends Actor with ActorLogging {
    BackupTrackerActors.store ! Subscribe

    def receive = {
      case OnChange(backups, sources) =>
        log.debug("BackupsActor.OnChange")
        Platform.runLater {
          backupsVBox.children = buildBackups(backups)
          sourceItems.clear()
          sourceItems ++= sources.map(s => s.path)
        }
    }
  }

  def apply(): BorderPane = {
    BackupTrackerActors.system.actorOf(Props(new BackupsActor))

    new BorderPane {
      styleClass = Seq("main")
      center = new VBox {
        spacing = 4
        children = List(
          backupsVBox,
          new Separator,
          new Button {
            text = "Add Backup"
            onAction = handle {addBackup}
          }
        )
      }
    }
  }

  private def buildBackups(backups: Seq[Backup]): mutable.MutableList[Node] = {
    if (backups.isEmpty)
      mutable.MutableList(SelectableLabel("No backups added"))
    else {
      val backupItems = backups.map(backup => {
        val description = s"${backup.sourcePath} -> ${backup.destinationPath}"
        SelectableLabel(description)
      })
      mutable.MutableList(backupItems: _*)
    }
  }

  private def addBackup = {
    val addBackupDialog = new Dialog[(String, String)]() {
      title = "Add Backup"
      headerText = "Select source and enter path for destination"
    }

    addBackupDialog.dialogPane().buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)

    val sourcesChoiceBox = new ChoiceBox(sourceItems)
    val destinationPathField = new TextField

    addBackupDialog.dialogPane().content = new HBox {
      spacing = 4
      children = List(
        new Label {
          text = "Source"
        },
        sourcesChoiceBox,
        new Separator,
        new Label {
          text = "Destination"
        },
        destinationPathField
      )
    }

    addBackupDialog.resultConverter = (dialogButton) => {
      if (dialogButton == ButtonType.OK)
        (sourcesChoiceBox.selectionModel().selectedItem.value, destinationPathField.text())
      else
        null
    }

    val store = BackupTrackerActors.store
    addBackupDialog.showAndWait() match {
      case Some((s: String, d: String)) => store ! AddBackup(Backup(s, d))
      case _ =>
    }
  }
}
