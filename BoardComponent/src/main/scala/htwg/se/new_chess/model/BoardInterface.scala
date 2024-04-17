package htwg.se.chess.model
package BoardComponent

import play.api.libs.json._

trait BoardInterface {

    def toJson(): JsObject

  // def load: Try[Option[BoardInterface]]
  // def save(grid: BoardInterface): Try[Unit]
  // def unbind(): Unit

}
