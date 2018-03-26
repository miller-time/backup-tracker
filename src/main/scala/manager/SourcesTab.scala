package manager

import scalafx.scene.control.Tab
import scalafx.scene.layout.VBox

object SourcesTab extends Tab {

  def apply() = {

    new Tab {
      text = "Sources"
      closable = false
      content = new VBox {
        children = List()
      }
    }
  }
}
