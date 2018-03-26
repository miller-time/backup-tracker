package manager

import scalafx.scene.control.Tab
import scalafx.scene.layout.VBox

object BackupLocationsTab extends Tab {

  def apply() = {

    new Tab {
      text = "Backup Locations"
      closable = false
      content = new VBox {
        children = List()
      }
    }
  }
}
