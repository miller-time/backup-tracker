package manager

import models.Store
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.VBox

object SourcesTab extends Tab {

  def apply(): Tab = {
    new Tab {
      text = "Sources"
      closable = false
      content = new VBox {
        padding = Insets(20)
        children = List(
          new Button {
            id = "addSourceButton"
            text = "Add Source"
            onAction = handle {addSource}
          }
        )
      }
    }
  }

  def addSource: Unit = {
    val addSourceDialog = new Dialog[String]() {
      title = "Add Source"
      headerText = "Enter path for new source"
    }

    addSourceDialog.dialogPane().buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)

    val sourcePath = new TextField
    addSourceDialog.dialogPane().content = sourcePath

    Platform.runLater(sourcePath.requestFocus())

    addSourceDialog.resultConverter = (dialogButton) => {
      if (dialogButton == ButtonType.OK)
        sourcePath.text()
      else
        null
    }

    addSourceDialog.showAndWait() match {
      case Some(s: String) => Store.addSource(s)
      case _ =>
    }
  }
}
