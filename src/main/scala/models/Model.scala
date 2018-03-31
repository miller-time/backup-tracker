package models

import scala.collection.mutable

object Model {
  type ChangeHandler[T] = Function[Seq[T], Unit]

  private val _sources: mutable.MutableList[Source] = mutable.MutableList(
    Source("C:\\Documents"),
    Source("C:\\Movies"),
    Source("C:\\Music")
  )

  def sources = _sources

  private val sourceChangeHandlers: mutable.MutableList[ChangeHandler[Source]] = mutable.MutableList()

  def addSource(path: String) = {
    _sources += Source(path)

    sourceChangeHandlers.foreach(h => h(sources))
  }

  def onSourcesChange(handler: ChangeHandler[Source]) = sourceChangeHandlers += handler

  private val _backupLocations: mutable.MutableList[BackupLocation] = mutable.MutableList(
    BackupLocation("/Volumes/BACKUPS"),
    BackupLocation("/Volumes/OFFSITE_BACKUPS"),
    BackupLocation("/Volumes/OFFSITE_BACKUPS/Movies"),
    BackupLocation("/Volumes/OFFSITE_BACKUPS/Music")
  )

  def backupLocations = _backupLocations

  private val backupLocationHandlers: mutable.MutableList[ChangeHandler[BackupLocation]] = mutable.MutableList()

  def addBackupLocation(path: String) = {
    _backupLocations += BackupLocation(path)

    backupLocationHandlers.foreach(h => h(backupLocations))
  }

  def onBackupLocationsChange(handler: ChangeHandler[BackupLocation]) = backupLocationHandlers += handler
}
