package xp5t5r

import unfiltered.request._
import unfiltered.response._
import unfiltered.filter.Plan
import org.json4s.native.JsonMethods.parseOpt

import javax.servlet.http.HttpServletRequest

import RestMappings._

object ApiPath {
  def unapply[T <: HttpServletRequest](req: HttpRequest[T]) = Some(req.uri.replaceFirst("api/v0/", "").split('?')(0))
}

// TODO: handle invalid ids (e.g. NumberFormatException)
class TextServiceRestApi extends Plan {
  private val ResourceName = "texts"
  private val textService = new TextService

  def intent: Plan.Intent = {
    case GET(ApiPath(Seg(ResourceName :: Nil))) => JsonContent ~> ResponseString(textService.fetchTexts)
    case GET(ApiPath(Seg(ResourceName :: id :: Nil))) => textService.fetchTextById(id) match {
      case Some(text) => JsonContent ~> ResponseString(text)
      case None => PlainTextContent ~> NotFound ~> ResponseString("Not Found!")
    }
    case req@PUT(ApiPath(Seg(ResourceName :: id :: Nil))) => parseOpt(Body.string(req)) match {
      case Some(json) => {
        textService.updateText(Text(id, json)) match {
          case Some(text) => JsonContent ~> NoContent ~> ResponseString("Text updated successfully.")
          case None => PlainTextContent ~> NotFound ~> ResponseString("Not Found!")
        }
      }
      case None => PlainTextContent ~> BadRequest ~> ResponseString("No content!?")
    }
    case req@POST(ApiPath(Seg(ResourceName :: Nil))) => parseOpt(Body.string(req)) match {
      case Some(json) => JsonContent ~> Created ~> ResponseString(textService.storeText(json))
      case None => PlainTextContent ~> BadRequest ~> ResponseString("No content!?")
    }
    case DELETE(ApiPath(Seg(ResourceName :: id :: Nil))) => textService.deleteTextById(id) match {
      case Some(text) => JsonContent ~> NoContent ~> ResponseString("Text deleted successfully.")
      case None => PlainTextContent ~> NotFound ~> ResponseString("Not Found!")
    }
    case req@ApiPath(Seg(ResourceName :: id :: _)) => PlainTextContent ~> NotFound ~> ResponseString("Unknown path: " + req.uri)
  }
}
