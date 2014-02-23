package xp5t5r

import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue

import scala.language.implicitConversions

object RestMappings {
  implicit val formats = DefaultFormats

  implicit def convertText2JsonString(text: Text): String = {
    val json = ("id" -> text.id) ~ ("content" -> text.content)
    compact(render(json))
  }

  implicit def convertTextId2JsonString(id: TextId): String = {
    val idString = id.id
    s"""{"id":"$idString"}"""
  }

  implicit def convertJsonContent2String(json: JValue): String = {
    (json \ "content").extract[String]
  }

  implicit def convertTextList2JsonString(texts: List[Text]): String = {
    val json = "texts" -> texts.map { t => ("id" -> t.id) ~ ("content" -> t.content)}
    compact(render(json))
  }

}
