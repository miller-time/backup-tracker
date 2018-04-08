import actors.BackupTrackerActors
import actors.StoreActor.{AddDestinations, AddSources}
import manager.Manager
import models._
import monitor.Monitor
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{BorderPane, VBox}

object BackupTracker extends JFXApp {

  initializeStore()

  stage = new PrimaryStage {
    title = "Backup Tracker"
    scene = new Scene(800, 600) {
      stylesheets = List(getClass.getResource("backupTracker.css").toExternalForm)
      root = new BorderPane {
        top = new VBox {
          center = new SplitPane {
            items ++= List(
              Manager(),
              Monitor()
            )
          }
        }
      }
    }
  }

  def initializeStore() = {
    val store = BackupTrackerActors.store

    val initialContents = StoreFileUtil.read()
    store ! AddSources(initialContents.backupSources.map(s => BackupSource(s)))
    store ! AddDestinations(initialContents.backupDestinations.map(d => BackupDestination(d)))
  }

  override def stopApp() = {
    BackupTrackerActors.system.terminate()
  }
}
