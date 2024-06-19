package htwg.se.chess
package model
package boardComponent
package boardStateTable

// import slick.jdbc.PostgresProfile.api._
import MyPostgresProfile.MyAPI._
// import htwg.se.chess.model.boardComponent.boardStateTable.MyPostgresProfile.MyAPI.strListTypeMapper
// import htwg.se.chess.model.boardComponent.boardStateTable.MyPostgresProfile.MyAPI.simpleStrListTypeMapper
// import scala.collection.immutable.Map
// import spray.json._
// import spray.json.DefaultJsonProtocol.mapFormat
// import spray.json.DefaultJsonProtocol.optionFormat
// import spray.json.DefaultJsonProtocol.listFormat
// import spray.json.DefaultJsonProtocol.StringJsonFormat
// import boardComponent.BoardJsonFormats._
import htwg.se.chess.model.boardComponent.boardBaseImpl.Board
import htwg.se.chess.model.boardComponent.boardBaseImpl.Coord
import htwg.se.chess.model.boardComponent.boardBaseImpl.Move
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.Piece
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.PieceType
import htwg.se.chess.model.boardComponent.boardBaseImpl.pieces.PieceColor


class BoardTable(tag: Tag) extends Table[Board](tag, "boards") {

  implicit val pieceColorMapper: BaseColumnType[PieceColor] = MappedColumnType.base[PieceColor, String](
    pc => pc.toString,
    str => PieceColor.valueOf(str)
  )

  implicit val pieceTypeMapper: BaseColumnType[PieceType] = MappedColumnType.base[PieceType, String](
    pt => pt.toString,
    str => PieceType.valueOf(str)
  )

  implicit val pieceMapper: BaseColumnType[Piece] = MappedColumnType.base[Piece, String](
    piece => s"${piece.getClass.getSimpleName.toUpperCase},${piece.color.toString},${piece.move_count}",
    str => {
      val Array(pieceType, color, moveCount) = str.split(",")
      Piece(PieceType.valueOf(pieceType), PieceColor.valueOf(color), moveCount.toInt)
    }
  )

  implicit val coordMapper: BaseColumnType[Coord] = MappedColumnType.base[Coord, String](
    coord => coord.toString,
    str => Coord.valueOf(str)
  )

  private def serializeSquares(squares: Map[Coord, Option[Piece]]): String = {
    squares.map { case (coord, pieceOpt) =>
      // val pieceStr = pieceOpt.map(piece => s"${piece.getClass.getSimpleName.toUpperCase},${piece.color.toString},${piece.move_count}").getOrElse("None")
      val pieceStr = pieceOpt.map(piece => s"hi,${piece.color.toString},${piece.move_count}").getOrElse("None")
      s"${coord.toString}:$pieceStr"
    }.mkString(";")
  }

  private def deserializeSquares(str: String): Map[Coord, Option[Piece]] = {
    str.split(";").map { entry =>
      val Array(coordStr, pieceStr) = entry.split(":")
      val coord = Coord.valueOf(coordStr)
      val pieceOpt = if (pieceStr == "None") None else {
        val Array(pieceType, color, moveCount) = pieceStr.split(",")
        Some(Piece(PieceType.valueOf(pieceType), PieceColor.valueOf(color), moveCount.toInt))
      }
      coord -> pieceOpt
    }.toMap
  }

  private def serializePieces(pieces: List[Piece]): String = {
    pieces.map(piece => s"${piece.getClass.getSimpleName.toUpperCase},${piece.color.toString},${piece.move_count}").mkString(";")
  }

  private def deserializePieces(str: String): List[Piece] = {
    str.split(";").map { pieceStr =>
      val Array(pieceType, color, moveCount) = pieceStr.split(",")
      Piece(PieceType.valueOf(pieceType), PieceColor.valueOf(color), moveCount.toInt)
    }.toList
  }

  implicit val squaresMapper: BaseColumnType[Map[Coord, Option[Piece]]] = MappedColumnType.base[Map[Coord, Option[Piece]], String](
    serializeSquares,
    deserializeSquares
  )

  implicit val piecesMapper: BaseColumnType[List[Piece]] = MappedColumnType.base[List[Piece], String](
    serializePieces,
    deserializePieces
  )

  // implicit val listStringMapper: BaseColumnType[List[String]] = MappedColumnType.base[List[String], String](
  //   list => list.mkString(","),
  //   str => str.split(",").toList
  // )


  // def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  // def squares = column[String]("squares")
  // def turn = column[PieceColor]("turn")
  // def inCheck = column[Boolean]("in_check")
  // def capturedPieces = column[String]("captured_pieces")
  // def moves = column[List[String]]("moves")
  // def * = (id, squares, turn, inCheck, capturedPieces, moves) <> ((Board.apply _).tupled, Board.unapply)

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def squares = column[Map[Coord, Option[Piece]]]("squares")
  def turn = column[PieceColor]("turn")
  // def turn = column[String]("turn")
  def inCheck = column[Boolean]("in_check")
  def capturedPieces = column[List[Piece]]("captured_pieces")
  def moves = column[List[String]]("moves")
  def * = (id, squares, turn, inCheck, capturedPieces, moves).mapTo[Board]
}
