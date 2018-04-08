package monitor

import actors.BackupTrackerActors
import actors.StoreActor.{OnChange, Subscribe}
import akka.actor.{Actor, ActorLogging, Props}
import models.{BackupDestination, BackupSource}
import scalafx.application.Platform
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

  class MonitorActor extends Actor with ActorLogging {
    BackupTrackerActors.store ! Subscribe

    def receive = {
      case OnChange(sources, destinations) =>
        log.debug("MonitorActor.OnChange")
        Platform.runLater {
          backupSourcesVBox.children = buildBackupSources(sources)
          backupDestinationsVBox.children = buildBackupDestinations(destinations)
        }
    }
  }

  def apply(): BorderPane = {
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

    BackupTrackerActors.system.actorOf(Props(new MonitorActor))

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

  private def buildBackupSources(backupSourceModels: Seq[BackupSource]): mutable.MutableList[Node] = {
    if (backupSourceModels.isEmpty)
      mutable.MutableList(buildTextField("No backup sources added"))
    else {
      val backupSources = backupSourceModels.map(backupSource => buildTextField(backupSource.name))
      mutable.MutableList(backupSources: _*)
    }
  }

  private def buildBackupDestinations(backupDestinationModels: Seq[BackupDestination]): mutable.MutableList[Node] = {
    if (backupDestinationModels.isEmpty)
      mutable.MutableList(buildTextField("No backup destinations added"))
    else {
      val backupDestinations = backupDestinationModels.map(backupDestination => buildTextField(backupDestination.name))
      mutable.MutableList(backupDestinations: _*)
    }
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
