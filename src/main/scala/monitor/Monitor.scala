package monitor

import java.io.File
import java.nio.file.{Files, Paths}

import actors.BackupTrackerActors
import actors.StoreActor.{OnChange, Subscribe}
import akka.actor.{Actor, ActorLogging, Props}
import components.{SectionLabel, SelectableLabel}
import models.Backup
import models.BackupStatus._
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

import scala.collection.mutable

object Monitor extends BorderPane {
  val backupsVBox: VBox = new VBox {
    spacing = 2
  }

  class MonitorActor extends Actor with ActorLogging {
    BackupTrackerActors.store ! Subscribe

    def receive = {
      case OnChange(backups, _) =>
        log.debug("MonitorActor.OnChange")
        Platform.runLater {
          backupsVBox.children = buildBackups(backups)
        }
    }
  }

  def apply(): BorderPane = {
    val backupsContainer = new VBox {
      spacing = 4
      children = List(
        SectionLabel("Backups"),
        new Separator,
        backupsVBox
      )
    }

    BackupTrackerActors.system.actorOf(Props(new MonitorActor))

    new BorderPane {
      styleClass = Seq("main")
      center = backupsContainer
    }
  }

  private def buildBackups(backupModels: Seq[Backup]): mutable.MutableList[Node] = {
    if (backupModels.isEmpty)
      mutable.MutableList(SelectableLabel("No backups added"))
    else {
      val backups = backupModels.map(backup => buildBackup(backup))
      mutable.MutableList(backups: _*)
    }
  }

  private def buildBackup(backup: Backup): HBox = {
    new HBox {
      spacing = 12
      children = List(
        buildBackupStatus(backup),
        buildBackupLabel(backup)
      )
    }
  }

  private def buildBackupStatus(backup: Backup): Button = {
    val color = checkStatus(backup) match {
      case UpToDate => Color.Green
      case OutOfDate => Color.Yellow
      case Offline => Color.Gray
    }
    new Button {
      styleClass = Seq("sleek-button")
      padding = Insets(1)
      graphic = new Circle {
        fill = color
        radius = 8
      }
    }
  }

  private def checkStatus(backup: Backup): BackupStatus = {
    val src = backup.sourcePath
    val srcExists = Files.exists(Paths.get(src))
    val srcGit = s"$src${File.separator}.git"
    val srcGitExists = Files.exists(Paths.get(srcGit))
    val dest = backup.destinationPath
    val destExists = Files.exists(Paths.get(dest))
    val destGit = s"$dest${File.separator}.git"
    val destGitExists = Files.exists(Paths.get(destGit))

    if (!srcExists || !srcGitExists || !destExists || !destGitExists)
      Offline
    else {
      // TODO: other checks
      println("source status:")
      println(sys.process.Process(Seq("git", "status", "--porcelain"), new File(src)).!!)

      println("dest status:")
      println(sys.process.Process(Seq("git", "status", "--porcelain"), new File(dest)).!!)
      UpToDate
    }
  }

  private def buildBackupLabel(backup: Backup): SelectableLabel = {
    val description = s"${backup.sourcePath} -> ${backup.destinationPath}"
    SelectableLabel(description)
  }
}
