package htwg.se.chess
package model
package boardComponent

import play.api.libs.json._

import boardComponent.boardBaseImpl.Board

trait BoardInterface {

    def startPos: Board
    def toJson(): JsObject

  // def load: Try[Option[BoardInterface]]
  // def save(grid: BoardInterface): Try[Unit]
  // def unbind(): Unit

}
