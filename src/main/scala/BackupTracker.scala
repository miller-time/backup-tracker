import manager.Manager
import monitor.Monitor
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{BorderPane, VBox}

object BackupTracker extends JFXApp {

  stage = new PrimaryStage {
    title = "Backup Tracker"
    scene = new Scene(800, 600) {
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
}
