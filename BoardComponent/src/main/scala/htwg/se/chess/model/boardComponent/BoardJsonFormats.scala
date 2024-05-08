package htwg.se.chess
package model
package boardComponent

// import BoardRegistry
import htwg.se.chess.model.boardComponent.boardBaseImpl.Board
import htwg.se.chess.model.boardComponent.boardBaseImpl.Coord
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.Piece
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.PieceType
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.PieceColor

import boardComponent.BoardRegistry.ActionPerformed
//#json-formats
// import spray.json.RootJsonFormat
// import spray.json.DefaultJsonProtocol
import spray.json._

object BoardJsonFormats {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  // implicit val boardJsonFormat: RootJsonFormat[Board]           = jsonFormat5(Board.apply)
  implicit val boardJsonFormat: RootJsonFormat[Board]           = jsonFormat(Board.apply, "id", "squares", "turn", "in_check", "captured_pieces", "moves")
  implicit val boardsJsonFormat: RootJsonFormat[Boards]           = jsonFormat1(Boards.apply)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed]  = jsonFormat1(ActionPerformed.apply)

  implicit object PieceJsonFormat extends RootJsonFormat[Piece] {
    def write(p: Piece) =
      JsArray(JsString(p.getClass.getSimpleName), JsString(p.color.toString), JsNumber(p.move_count))

    def read(value: JsValue) = value match {
      case JsArray(Vector(JsString(pt), JsString(pc), JsNumber(mc))) =>
        Piece(PieceType.valueOf(pt.toLowerCase.capitalize), PieceColor.valueOf(pc), mc.toInt)
      case _           => deserializationError("Piece expected")
    }
  }

  implicit object CoordJsonFormat extends RootJsonFormat[Coord] {
    def write(c: Coord) =
      JsString(c.toString)

    def read(value: JsValue) = value match {
      case JsString(c) =>
        Coord.valueOf(c)
      case _           => deserializationError("Coord expected")
    }
  }

  implicit object PieceTypeJsonFormat extends RootJsonFormat[PieceType] {
    def write(pt: PieceType) =
      JsString(pt.toString)

    def read(value: JsValue) = value match {
      case JsString(pt) =>
        PieceType.valueOf(pt)
      case _           => deserializationError("PieceType expected")
    }
  }

  implicit object PieceColorJsonFormat extends RootJsonFormat[PieceColor] {
    def write(pc: PieceColor) =
      JsString(pc.toString)

    def read(value: JsValue) = value match {
      case JsString(pc) =>
        PieceColor.valueOf(pc)
      case _           => deserializationError("PieceColor expected")
    }
  }
}
//#json-formats
