package xp5t5r

import unfiltered.request._
import unfiltered.response._

class EchoFilter extends unfiltered.filter.Plan {
  def intent = {
    case Path(Seg("echo" :: p :: Nil)) => ResponseString(p)
  }
}
