package manager

import actors.BackupTrackerActors
import actors.StoreActor.AddDestination
import models.BackupDestination
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.VBox

object BackupDestinationsTab extends Tab {

  def apply(): Tab = {
    new Tab {
      text = "Backup Destinations"
      closable = false
      content = new VBox {
        padding = Insets(20)
        children = List(
          new Button {
            id = "addBackupDestinationButton"
            text = "Add Backup Destination"
            onAction = handle {addBackupDestination}
          }
        )
      }
    }
  }

  def addBackupDestination: Unit = {
    val addBackupDestinationDialog = new Dialog[String]() {
      title = "Add Backup Destination"
      headerText = "Enter path for new backup destination"
    }

    addBackupDestinationDialog.dialogPane().buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)

    val backupDestinationPath = new TextField
    addBackupDestinationDialog.dialogPane().content = backupDestinationPath

    Platform.runLater(backupDestinationPath.requestFocus())

    addBackupDestinationDialog.resultConverter = (dialogButton) => {
      if (dialogButton == ButtonType.OK)
        backupDestinationPath.text()
      else
        null
    }

    val store = BackupTrackerActors.store
    addBackupDestinationDialog.showAndWait() match {
      case Some(b: String) => store ! AddDestination(BackupDestination(b))
      case _ =>
    }
  }
}
