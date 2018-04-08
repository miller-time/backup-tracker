package models

object BackupStatus extends Enumeration {
  type BackupStatus = Value
  val UpToDate, OutOfDate, Offline = Value
}
