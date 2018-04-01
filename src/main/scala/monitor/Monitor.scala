package monitor

import models.Store.ChangeHandler
import models.{BackupLocation, Source, Store}
import scalafx.scene.Node
import scalafx.scene.control.{Label, Separator, TextField}
import scalafx.scene.layout.{BorderPane, VBox}

import scala.collection.mutable

object Monitor extends BorderPane {

  val sourcesVBox: VBox = new VBox {
    spacing = 2
  }

  val backupLocationsVBox: VBox = new VBox {
    spacing = 2
  }

  def apply() = {
    val sourcesContainer = new VBox {
      spacing = 4
      children = List(
        buildSectionLabel("Sources"),
        new Separator,
        sourcesVBox
      )
    }

    val backupLocationsContainer = new VBox {
      spacing = 4
      children = List(
        buildSectionLabel("Backup Locations"),
        new Separator,
        backupLocationsVBox
      )
    }

    initChangeHandler()

    new BorderPane {
      styleClass = Seq("monitor")
      top = new VBox {
        spacing = 10
        children = List(
          sourcesContainer,
          backupLocationsContainer
        )
      }
    }
  }

  def initChangeHandler() = {
    val handler: ChangeHandler = {
      case Left(m) => sourcesVBox.children = buildSources(m)
      case Right(m) => backupLocationsVBox.children = buildBackupLocations(m)
    }
    Store.onChange(handler)
  }

  private def buildSources(sourceModels: Seq[Source]): mutable.MutableList[Node] = {
    if (Store.sources.isEmpty)
      mutable.MutableList(buildTextField("No sources added"))
    else
      Store.sources.map(source => buildTextField(source.name))
  }

  private def buildBackupLocations(backupLocationModels: Seq[BackupLocation]): mutable.MutableList[Node] = {
    if (Store.backupLocations.isEmpty)
      mutable.MutableList(buildTextField("No backup locations added"))
    else
      Store.backupLocations.map(backupLocation => buildTextField(backupLocation.name))
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
