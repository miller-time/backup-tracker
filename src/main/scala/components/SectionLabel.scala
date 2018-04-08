package components

import scalafx.scene.control.Label

object SectionLabel extends Label {
  def apply(textContent: String): Label = {
    new Label {
      text = textContent
      styleClass = Seq("header")
    }
  }
}
