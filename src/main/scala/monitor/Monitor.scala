package monitor

import models.{BackupLocation, Model, Source}
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
        new Label {
          text = "Sources"
          styleClass = Seq("monitor-header")
        },
        new Separator,
        sourcesVBox
      )
    }

    val backupLocationsContainer = new VBox {
      spacing = 4
      children = List(
        new Label {
          text = "Backup Locations"
          styleClass = Seq("monitor-header")
        },
        new Separator,
        backupLocationsVBox
      )
    }

    initChangeHandlers()

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

  def initChangeHandlers() = {
    val sourcesChanged = (s: Seq[Source]) => {
      sourcesVBox.children = buildSources(s)
    }
    Model.onSourcesChange(sourcesChanged)
    sourcesChanged(List())

    val backupLocationsChanged = (b: Seq[BackupLocation]) => {
      backupLocationsVBox.children = buildBackupLocations(b)
    }
    Model.onBackupLocationsChange(backupLocationsChanged)
    backupLocationsChanged(List())
  }

  private def buildSources(sourceModels: Seq[Source]): mutable.MutableList[Node] = {
    Model.sources.map(source => new TextField {
      text = source.name
      editable = false
      styleClass = Seq("monitor-text")
    })
  }

  private def buildBackupLocations(backupLocationModels: Seq[BackupLocation]): mutable.MutableList[Node] = {
    Model.backupLocations.map(backupLocation => new TextField {
      text = backupLocation.name
      editable = false
      styleClass = Seq("monitor-text")
    })
  }
}
