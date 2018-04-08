package components

import scalafx.scene.control.TextField

object SelectableLabel extends TextField {
  def apply(textContent: String): TextField = {
    new TextField {
      text = textContent
      editable = false
      styleClass = Seq("text")
    }
  }
}
