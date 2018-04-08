package manager

import scalafx.scene.control.TabPane

object Manager extends TabPane {

  def apply(): TabPane = {
    val backupSources = BackupSourcesTab()
    val backupDestinations = BackupDestinationsTab()

    new TabPane {
      tabs = List(
        backupSources,
        backupDestinations
      )
    }
  }
}
