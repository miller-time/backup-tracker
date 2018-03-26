package manager

import scalafx.scene.control.TabPane

object Manager extends TabPane {

  def apply() = {
    val sources = SourcesTab()
    val backupLocations = BackupLocationsTab()

    new TabPane {
      tabs = List(
        sources,
        backupLocations
      )
    }
  }
}
