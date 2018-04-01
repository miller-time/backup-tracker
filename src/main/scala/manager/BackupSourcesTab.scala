package manager

import models.Store
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.VBox

object BackupSourcesTab extends Tab {

  def apply(): Tab = {
    new Tab {
      text = "Backup Sources"
      closable = false
      content = new VBox {
        padding = Insets(20)
        children = List(
          new Button {
            id = "addBackupSourceButton"
            text = "Add Backup Source"
            onAction = handle {addBackupSource}
          }
        )
      }
    }
  }

  def addBackupSource: Unit = {
    val addBackupSourceDialog = new Dialog[String]() {
      title = "Add Backup Source"
      headerText = "Enter path for new backup source"
    }

    addBackupSourceDialog.dialogPane().buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)

    val backupSourcePath = new TextField
    addBackupSourceDialog.dialogPane().content = backupSourcePath

    Platform.runLater(backupSourcePath.requestFocus())

    addBackupSourceDialog.resultConverter = (dialogButton) => {
      if (dialogButton == ButtonType.OK)
        backupSourcePath.text()
      else
        null
    }

    addBackupSourceDialog.showAndWait() match {
      case Some(s: String) => Store.addBackupSource(s)
      case _ =>
    }
  }
}
