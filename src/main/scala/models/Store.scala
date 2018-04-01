package models

import java.io.File

import scala.collection.mutable
import scala.io.Source

object Store {

  type ModelValues = Either[Seq[BackupSource], Seq[BackupDestination]]
  type ChangeHandler = (ModelValues) => Unit

  val json = readStoreFile()
  json match {
    case Some(s) => println(s)
    case None => println("file not found")
  }

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
  private val _backupSources: mutable.MutableList[BackupSource] = mutable.MutableList()

  def backupSources = _backupSources

  def addBackupSource(path: String) = {
    _backupSources += BackupSource(path)

    changeHandlers.foreach(handler => handler(Left(backupSources)))
  }

  /*
   * backup destinations (destinations to save backups)
   */
  private val _backupDestinations: mutable.MutableList[BackupDestination] = mutable.MutableList()

  def backupDestinations = _backupDestinations

  def addBackupDestination(path: String) = {
    _backupDestinations += BackupDestination(path)

    changeHandlers.foreach(handler => handler(Right(backupDestinations)))
  }

  def readStoreFile(): Option[String] = {
    val storeFilePath = System.getProperty("user.home")

    try {
      val source = Source.fromFile(storeFilePath + File.separator + ".backup-tracker.json")
      val contents = source.mkString
      source.close()
      Some(contents)
    } catch {
      case _: java.io.FileNotFoundException => None
    }
  }
}
