package monitor

import scalafx.scene.layout.VBox

object Monitor extends VBox {

  def apply() = {
    new VBox {
      children = List()
    }
  }
}
