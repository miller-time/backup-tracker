package models

import java.io.File

import org.json4s._
import org.json4s.native.JsonMethods._

import scala.io.Source

object StoreFileUtil {

  private val storeFilePath = System.getProperty("user.home") + File.separator + ".backup-tracker.json"

  case class StoreContents(
    backupSources: List[String] = List.empty,
    backupDestinations: List[String] = List.empty
  )

  def read(): StoreContents = {
    readStoreFile() match {
      case Some(fileContents) => {
        parse(fileContents) match {
          case parsed: JObject =>
            val backupSources = parsed.values("sources") match {
              case parsedSources: List[_] => parsedSources.map(_.toString)
              case _ =>
                println("[warning] no sources found in store file")
                List[String]()
            }
            val backupDestinations = parsed.values("destinations") match {
              case parsedDestinations: List[_] => parsedDestinations.map(_.toString)
              case _ =>
                println("[warning] no destinations found in store file")
                List[String]()
            }
            StoreContents(backupSources, backupDestinations)
          case _ =>
            println("[warning] unable to parse store file")
            StoreContents()
        }
      }
      case None =>
        println("[info] no store file found")
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
}
