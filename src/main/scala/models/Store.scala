package models

import models.StoreFileUtil.StoreContents

import scala.collection.mutable

object Store {

  type ModelValues = Either[Seq[BackupSource], Seq[BackupDestination]]
  type ChangeHandler = (ModelValues) => Unit

  val storeContents = StoreFileUtil.read()

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
    handler(Left(backupSources))
    handler(Right(backupDestinations))
  }

  /*
   * backup sources (files to back up)
   */
  private val _backupSources: mutable.MutableList[BackupSource] =
    mutable.MutableList() ++ storeContents.backupSources.map(BackupSource)

  def backupSources = _backupSources

  def addBackupSource(path: String) = {
    _backupSources += BackupSource(path)

    changeHandlers.foreach(handler => handler(Left(backupSources)))
  }

  /*
   * backup destinations (destinations to save backups)
   */
  private val _backupDestinations: mutable.MutableList[BackupDestination] =
    mutable.MutableList() ++ storeContents.backupDestinations.map(BackupDestination)

  def backupDestinations = _backupDestinations

  def addBackupDestination(path: String) = {
    _backupDestinations += BackupDestination(path)

    changeHandlers.foreach(handler => handler(Right(backupDestinations)))
  }
}
