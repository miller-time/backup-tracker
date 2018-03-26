package models

import scala.collection.mutable

object Model {

  val sources: mutable.MutableList[Source] = mutable.MutableList(
    Source("C:\\Documents"),
    Source("C:\\Movies"),
    Source("C:\\Music")
  )
  val backupLocations: mutable.MutableList[BackupLocation] = mutable.MutableList(
    BackupLocation("/Volumes/BACKUPS"),
    BackupLocation("/Volumes/OFFSITE_BACKUPS"),
    BackupLocation("/Volumes/OFFSITE_BACKUPS/Movies"),
    BackupLocation("/Volumes/OFFSITE_BACKUPS/Music")
  )
}
