package manager

import models.Store
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.VBox

object BackupLocationsTab extends Tab {

  def apply(): Tab = {
    new Tab {
      text = "Backup Locations"
      closable = false
      content = new VBox {
        padding = Insets(20)
        children = List(
          new Button {
            id = "addBackupLocationButton"
            text = "Add Backup Location"
            onAction = handle {addBackupLocation}
          }
        )
      }
    }
  }

  def addBackupLocation: Unit = {
    val addBackupLocationDialog = new Dialog[String]() {
      title = "Add Backup Location"
      headerText = "Enter path for new backup location"
    }

    addBackupLocationDialog.dialogPane().buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)

    val backupLocationPath = new TextField
    addBackupLocationDialog.dialogPane().content = backupLocationPath

    Platform.runLater(backupLocationPath.requestFocus())

    addBackupLocationDialog.resultConverter = (dialogButton) => {
      if (dialogButton == ButtonType.OK)
        backupLocationPath.text()
      else
        null
    }

    addBackupLocationDialog.showAndWait() match {
      case Some(b: String) => Store.addBackupLocation(b)
      case _ =>
    }
  }
}
