import actors.BackupTrackerActors
import actors.StoreActor.{AddBackups, AddSources}
import backups.Backups
import models._
import monitor.Monitor
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.layout.{BorderPane, VBox}
import sources.Sources

object BackupTracker extends JFXApp {

  initializeStore()

  stage = new PrimaryStage {
    title = "Backup Tracker"
    scene = new Scene(800, 600) {
      stylesheets = List(getClass.getResource("backupTracker.css").toExternalForm)
      root = new BorderPane {
        top = new VBox {
          center = new TabPane {
            tabs = List(
              new Tab {
                text = "Monitor"
                closable = false
                content = Monitor()
              },
              new Tab {
                text = "Sources"
                closable = false
                content = Sources()
              },
              new Tab {
                text = "Backups"
                closable = false
                content = Backups()
              }
            )
          }
        }
      }
    }
  }

  def initializeStore() = {
    val store = BackupTrackerActors.store

    val initialContents = StoreFileUtil.read()
    store ! AddBackups(initialContents.backups.map(b => Backup(b._1, b._2)))
    store ! AddSources(initialContents.backupSources.map(s => BackupSource(s)))
  }

  override def stopApp() = {
    BackupTrackerActors.system.terminate()
  }
}
