package htwg.se.new_chess.model.boardComponent

import htwg.se.new_chess.model.boardComponent.pieces.{Pawn, Rook, Knight, Bishop, Queen, King}
import PieceType.*
import PieceColor.*
import Coord.*

case class Board(squares: Vector[Square]) {

  def updateSquare(newSquare: Square): Board = {
    val index = squares.indexWhere(_.coord == newSquare.coord)
    if (index >= 0)
      Board(squares.updated(index, newSquare))
    else
      this // Cell not found, return the current instance
  }

  def isMoveValid(startPos: Coord, endPos: Coord): Boolean = {
    val pieceOpt: Option[Piece] = squares.find(_.coord == startPos).flatMap(_.piece)
    pieceOpt match {
      case Some(piece) => piece.isMoveValid(startPos, endPos)
      case None        => false // No piece at the starting position
    }
  }

  def startingPoition(): Board = {
    // val squares = Board.startingPosition.foldLeft(Vector.empty[Square]) {
    //   case (acc, (pos, piece)) =>
    //     acc :+ Square(pos, Some(piece))
    // }
    val squares = Coord.values.foldLeft(Vector.empty[Square]) { (acc, coord) =>
      val pieceOpt: Option[Piece] = Board.startingPosition.find(_(0) == coord).map(_(1))
      // val pieceOpt = Board.startingPosition.find(_(0) == coord).flatMap(_.piece)
      acc :+ Square(coord, pieceOpt)
    }
    Board(squares)
  }

  override def toString(): String = {
    this.squares
      .sortBy(_.coord.print_ord)
      .map(_.piece.getOrElse("-"))
      .grouped(Coord.len)
      .map(_.mkString(" "))
      .mkString("\n")
  }
}

object Board {
  def apply() = {
    new Board(Coord.values.map(Square(_, None)).toVector)
  }

  val startingPosition = List(
    (A1, Piece(ROOK, WHITE)),
    (B1, Piece(KNIGHT, WHITE)),
    (C1, Piece(BISHOP, WHITE)),
    (D1, Piece(QUEEN, WHITE)),
    (E1, Piece(KING, WHITE)),
    (F1, Piece(BISHOP, WHITE)),
    (G1, Piece(KNIGHT, WHITE)),
    (H1, Piece(ROOK, WHITE)),
    (A2, Piece(PAWN, WHITE)),
    (B2, Piece(PAWN, WHITE)),
    (C2, Piece(PAWN, WHITE)),
    (D2, Piece(PAWN, WHITE)),
    (E2, Piece(PAWN, WHITE)),
    (F2, Piece(PAWN, WHITE)),
    (G2, Piece(PAWN, WHITE)),
    (H2, Piece(PAWN, WHITE)),
    (A8, Piece(ROOK, BLACK)),
    (B8, Piece(KNIGHT, BLACK)),
    (C8, Piece(BISHOP, BLACK)),
    (D8, Piece(QUEEN, BLACK)),
    (E8, Piece(KING, BLACK)),
    (F8, Piece(BISHOP, BLACK)),
    (G8, Piece(KNIGHT, BLACK)),
    (H8, Piece(ROOK, BLACK)),
    (A7, Piece(PAWN, BLACK)),
    (B7, Piece(PAWN, BLACK)),
    (C7, Piece(PAWN, BLACK)),
    (D7, Piece(PAWN, BLACK)),
    (E7, Piece(PAWN, BLACK)),
    (F7, Piece(PAWN, BLACK)),
    (G7, Piece(PAWN, BLACK)),
    (H7, Piece(PAWN, BLACK))
  )
}
