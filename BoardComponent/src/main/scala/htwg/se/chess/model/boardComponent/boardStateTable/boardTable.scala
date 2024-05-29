package htwg.se.chess
package model
package boardComponent
package boardStateTable

import slick.jdbc.PostgresProfile.api.*
import spray.json._
import spray.json.DefaultJsonProtocol.mapFormat
import spray.json.DefaultJsonProtocol.optionFormat
import spray.json.DefaultJsonProtocol.listFormat
import spray.json.DefaultJsonProtocol.StringJsonFormat
import boardComponent.BoardJsonFormats._
import htwg.se.chess.model.boardComponent.boardBaseImpl.Board
import htwg.se.chess.model.boardComponent.boardBaseImpl.Coord
import htwg.se.chess.model.boardComponent.boardBaseImpl.Move
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.Piece
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.PieceType
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.PieceColor


class BoardTable(tag: Tag) extends Table[Board](tag, "boards") {
  implicit val mapStringOptionPieceMapper: BaseColumnType[Map[Coord, Option[Piece]]] = MappedColumnType.base[Map[Coord, Option[Piece]], String](
    map => map.toJson.toString,
    str => str.parseJson.convertTo[Map[Coord, Option[Piece]]]
  )

  implicit val listPieceMapper: BaseColumnType[List[Piece]] = MappedColumnType.base[List[Piece], String](
    list => list.toJson.toString,
    str => str.parseJson.convertTo[List[Piece]]
  )

  implicit val listStringMapper: BaseColumnType[List[String]] = MappedColumnType.base[List[String], String](
    list => list.toJson.toString,
    str => str.parseJson.convertTo[List[String]]
  )

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def squares = column[Map[Coord, Option[Piece]]]("squares")
  // def turn = column[PieceColor]("turn")
  def turn = column[String]("turn")
  def in_check = column[Boolean]("in_check")
  def captured_pieces = column[List[Piece]]("captured_pieces")
  def moves = column[List[String]]("moves")

  def * = (id, squares, turn, in_check, captured_pieces, moves).mapTo[Board]
  // def * = (id, squares, turn, in_check, captured_pieces, moves) <> (Board.tupled, Board.unapply)
}
