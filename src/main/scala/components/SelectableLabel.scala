package components

import scalafx.scene.control.TextField
import scalafx.scene.layout.Priority

object SelectableLabel {
  def apply(textContent: String): SelectableLabel = {
    new SelectableLabel(textContent)
  }
}

class SelectableLabel(textContent: String) extends TextField {
  text = textContent
  editable = false
  hgrow = Priority.Always
  styleClass = Seq("text")
}
