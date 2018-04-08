package sources

import actors.BackupTrackerActors
import actors.StoreActor.{AddSource, OnChange, Subscribe}
import akka.actor.{Actor, ActorLogging, Props}
import components.SelectableLabel
import models.BackupSource
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, VBox}

import scala.collection.mutable

object Sources extends BorderPane {
  val sourcesVBox: VBox = new VBox {
    spacing = 2
  }

  class SourcesActor extends Actor with ActorLogging {
    BackupTrackerActors.store ! Subscribe

    def receive = {
      case OnChange(_, sources) =>
        log.debug("SourcesActor.OnChange")
        Platform.runLater {
          sourcesVBox.children = buildSources(sources)
        }
    }
  }

  def apply(): BorderPane = {
    BackupTrackerActors.system.actorOf(Props(new SourcesActor))

    new BorderPane {
      styleClass = Seq("main")
      center = new VBox {
        spacing = 4
        children = List(
          sourcesVBox,
          new Separator,
          new Button {
            text = "Add Source"
            onAction = handle {addSource}
          }
        )
      }
    }
  }

  private def buildSources(sources: Seq[BackupSource]): mutable.MutableList[Node] = {
    if (sources.isEmpty)
      mutable.MutableList(SelectableLabel("No sources added"))
    else {
      val sourceLabels = sources.map(source => SelectableLabel(source.path))
      mutable.MutableList(sourceLabels: _*)
    }
  }

  private def addSource = {
    val addSourceDialog = new Dialog[String]() {
      title = "Add Source"
      headerText = "Enter path for new source"
    }

    addSourceDialog.dialogPane().buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)

    val sourcePathField = new TextField
    addSourceDialog.dialogPane().content = sourcePathField

    Platform.runLater(sourcePathField.requestFocus())

    addSourceDialog.resultConverter = (dialogButton) => {
      if (dialogButton == ButtonType.OK)
        sourcePathField.text()
      else
        null
    }

    val store = BackupTrackerActors.store
    addSourceDialog.showAndWait() match {
      case Some(s: String) => store ! AddSource(BackupSource(s))
      case _ =>
    }
  }
}
