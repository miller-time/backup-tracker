import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color._

object BackupTracker extends JFXApp {

  stage = new PrimaryStage {
    title = "Backup Tracker"
    scene = new Scene {
      fill = Black
    }
  }
}
