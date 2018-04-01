package models

import scala.collection.mutable

object Store {

  type ModelValues = Either[Seq[Source], Seq[BackupLocation]]
  type ChangeHandler = (ModelValues) => Unit

  /*
   * internal list of change handlers that have been registered
   */
  private val changeHandlers: mutable.MutableList[ChangeHandler] = mutable.MutableList()

  /*
   * method for registering a change handler
   */
  def onChange(handler: ChangeHandler) = {
    changeHandlers += handler

    // initialize caller with current state
    handler(Left(sources))
    handler(Right(backupLocations))
  }

  /*
   * backup sources (files to back up)
   */
  private val _sources: mutable.MutableList[Source] = mutable.MutableList()

  def sources = _sources

  def addSource(path: String) = {
    _sources += Source(path)

    changeHandlers.foreach(handler => handler(Left(sources)))
  }

  /*
   * backup locations (destinations to save backups)
   */
  private val _backupLocations: mutable.MutableList[BackupLocation] = mutable.MutableList(
//    BackupLocation("/Volumes/BACKUPS"),
//    BackupLocation("/Volumes/OFFSITE_BACKUPS"),
//    BackupLocation("/Volumes/OFFSITE_BACKUPS/Movies"),
//    BackupLocation("/Volumes/OFFSITE_BACKUPS/Music")
  )

  def backupLocations = _backupLocations

  def addBackupLocation(path: String) = {
    _backupLocations += BackupLocation(path)

    changeHandlers.foreach(handler => handler(Right(backupLocations)))
  }
}
