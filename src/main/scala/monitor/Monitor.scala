package monitor

import models.Model
import scalafx.scene.control.{Label, Separator, TextField}
import scalafx.scene.layout.{BorderPane, VBox}

object Monitor extends BorderPane {

  def apply() = {
    val sources = new VBox {
      spacing = 4
      children = List(
        new Label {
          text = "Sources"
          styleClass = Seq("monitor-header")
        },
        new Separator,
        new VBox {
          spacing = 2
          children = Model.sources.map(source => new TextField {
            text = source.name
            editable = false
            styleClass = Seq("monitor-text")
          })
        }
      )
    }

    val backupLocations = new VBox {
      spacing = 4
      children = List(
        new Label {
          text = "Backup Locations"
          styleClass = Seq("monitor-header")
        },
        new Separator,
        new VBox {
          spacing = 2
          children = Model.backupLocations.map(backupLocation => new TextField {
            text = backupLocation.name
            editable = false
            styleClass = Seq("monitor-text")
          })
        }
      )
    }

    new BorderPane {
      styleClass = Seq("monitor")
      top = new VBox {
        spacing = 10
        children = List(
          sources,
          backupLocations
        )
      }
    }
  }
}
