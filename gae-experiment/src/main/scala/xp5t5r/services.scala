package xp5t5r

import java.util.logging.Logger

import scala.collection.JavaConversions._

import com.google.appengine.api.datastore._

case class TextId(id: String)
case class Text(id: String, content: String)

class TextService {
  private[this] lazy val logger = Logger.getLogger(getClass.getName)
  private lazy val datastore: DatastoreService = DatastoreServiceFactory.getDatastoreService
  private lazy val parentKey = KeyFactory.createKey("Texts", "textBucket")

  private def convertEntityToText(entity: Entity): Text = {
    Text(entity.getKey.getId.toString, entity.getProperty("content").toString)
  }

  private def convertEntityListToTextList(entities: List[Entity]): List[Text] = {
    entities.map( e => Text(e.getKey.getId.toString, e.getProperty("content").toString))
  }

  private def getTextIfExists(id: String): Option[Text] = {
    val textKey: Key = KeyFactory.createKey(parentKey, "Text", id.toLong)
    logger.info("keyToString: " + KeyFactory.keyToString(textKey))
    try {
      val entity: Entity = datastore.get(textKey)
      Some(convertEntityToText(entity))
    } catch {
      case ex: EntityNotFoundException => {
        logger.warning("Entity not found with id: " + id)
        None
      }
    }
  }

  def fetchTextById(id: String): Option[Text] = {
    getTextIfExists(id)
  }

  def fetchTexts: List[Text] = {
    val query: Query = new Query("Text")
    val texts: List[Text] = convertEntityListToTextList(datastore.prepare(query).asList(FetchOptions.Builder.withDefaults).toList)
    logger.info("Amount of entities: " + texts.size)
    logger.info("Got entities: " + texts.mkString(","))
    texts
  }

  def storeText(content: String): TextId = {
    val text: Entity = new Entity("Text", parentKey)
    text.setProperty("content", content)
    datastore.put(text)
    logger.info("Generated text key: " + text.getKey.getId)
    TextId(text.getKey.getId.toString)
  }

  def updateText(newText: Text): Option[Text] = {
    getTextIfExists(newText.id) match {
      case Some(oldText) =>
        val entity: Entity = new Entity("Text", oldText.id.toLong, parentKey)
        entity.setProperty("content", newText.content)
        datastore.put(entity)
        Some(convertEntityToText(entity))
      case None => None
    }
  }

  def deleteTextById(id: String): Option[Text] = {
    getTextIfExists(id) match {
      case Some(oldText) =>
        val textKey: Key = KeyFactory.createKey(parentKey, "Text", id.toLong)
        datastore.delete(textKey)
        Some(oldText)
      case None => None
    }
  }
}
