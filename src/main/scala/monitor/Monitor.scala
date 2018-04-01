package monitor

import models.Store.ChangeHandler
import models.{BackupDestination, BackupSource, Store}
import scalafx.scene.Node
import scalafx.scene.control.{Label, Separator, TextField}
import scalafx.scene.layout.{BorderPane, VBox}

import scala.collection.mutable

object Monitor extends BorderPane {

  val backupSourcesVBox: VBox = new VBox {
    spacing = 2
  }

  val backupDestinationsVBox: VBox = new VBox {
    spacing = 2
  }

  def apply() = {
    val backupSourcesContainer = new VBox {
      spacing = 4
      children = List(
        buildSectionLabel("Backup Sources"),
        new Separator,
        backupSourcesVBox
      )
    }

    val backupDestinationsContainer = new VBox {
      spacing = 4
      children = List(
        buildSectionLabel("Backup Destinations"),
        new Separator,
        backupDestinationsVBox
      )
    }

    initChangeHandler()

    new BorderPane {
      styleClass = Seq("monitor")
      top = new VBox {
        spacing = 10
        children = List(
          backupSourcesContainer,
          backupDestinationsContainer
        )
      }
    }
  }

  def initChangeHandler() = {
    val handler: ChangeHandler = {
      case Left(m) => backupSourcesVBox.children = buildBackupSources(m)
      case Right(m) => backupDestinationsVBox.children = buildBackupDestinations(m)
    }
    Store.onChange(handler)
  }

  private def buildBackupSources(backupSourceModels: Seq[BackupSource]): mutable.MutableList[Node] = {
    if (Store.backupSources.isEmpty)
      mutable.MutableList(buildTextField("No backup sources added"))
    else
      Store.backupSources.map(backupSource => buildTextField(backupSource.name))
  }

  private def buildBackupDestinations(backupDestinationModels: Seq[BackupDestination]): mutable.MutableList[Node] = {
    if (Store.backupDestinations.isEmpty)
      mutable.MutableList(buildTextField("No backup destinations added"))
    else
      Store.backupDestinations.map(backupDestination => buildTextField(backupDestination.name))
  }

  private def buildSectionLabel(textContent: String): Label = {
    new Label {
      text = textContent
      styleClass = Seq("monitor-header")
    }
  }

  private def buildTextField(textContent: String): TextField = {
    new TextField {
      text = textContent
      editable = false
      styleClass = Seq("monitor-text")
    }
  }
}
