package models

import java.io.{File, PrintWriter}

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.io.Source

object StoreFileUtil {

  private val storeFilePath = System.getProperty("user.home") + File.separator + ".backup-tracker.json"

  case class StoreContents(
    backups: List[(String, String)] = List.empty,
    backupSources: List[String] = List.empty
  )

  def read(): StoreContents = {
    readStoreFile() match {
      case Some(fileContents) =>
        try {
          parseStoreFileContents(fileContents)
        } catch {
          case _: NoSuchElementException =>
            println("[info] unable to parse store file")
            StoreContents()
        }
      case None =>
        println("[info] no store file found")
        StoreContents()
    }
  }

  def write(storeContents: StoreContents) = {
    writeStoreFile(pretty(render(
      ("backups" -> storeContents.backups.map { b =>
        ("source" -> b._1) ~
        ("destination" -> b._2)
      }) ~
      ("sources" -> storeContents.backupSources)
    )))
  }

  private def parseStoreFileContents(fileContents: String): StoreContents = {
    parse(fileContents) match {
      case parsed: JObject =>
        val backups: List[(String, String)] = parsed.values("backups") match {
          case parsedBackups: List[_] =>
            parsedBackups.asInstanceOf[List[Map[String, String]]]
              .map(backup => (backup("source"), backup("destination")))
          case _ =>
            println("[warning] no backups found in store file")
            List[(String, String)]()
        }
        val backupSources = parsed.values("sources") match {
          case parsedSources: List[_] => parsedSources.map(_.toString)
          case _ =>
            println("[warning] no sources found in store file")
            List[String]()
        }
        StoreContents(backups, backupSources)
      case _ =>
        println("[warning] unable to parse store file")
        StoreContents()
    }
  }

  private def readStoreFile(): Option[String] = {
    try {
      val source = Source.fromFile(storeFilePath)
      val contents = source.mkString
      source.close()
      Some(contents)
    } catch {
      case _: java.io.FileNotFoundException => None
    }
  }

  private def writeStoreFile(json: String) = {
    val writer = new PrintWriter(new File(storeFilePath))
    writer.write(json)
    writer.close()
  }
}
