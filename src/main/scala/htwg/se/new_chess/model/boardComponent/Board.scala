package htwg.se.new_chess.model.boardComponent

import htwg.se.new_chess.model.boardComponent.pieces.{Pawn, Rook, Knight, Bishop, Queen, King}
import htwg.se.new_chess.model.boardComponent.SquareExtensions._
import htwg.se.new_chess.model.boardComponent.SquareExtensions.Removable._
import htwg.se.new_chess.model.boardComponent.SquareExtensions.Addable._
import htwg.se.new_chess.model.boardComponent.SquareExtensions.Squareable._
import htwg.se.new_chess.model.boardComponent.PieceType.*
import htwg.se.new_chess.model.boardComponent.PieceColor.*
import htwg.se.new_chess.model.boardComponent.Coord.*

case class Board(squares: Vector[Square]) {

  def updateSquare(new_square: Square): Board = {
    val index = squares.indexWhere(_.coord == new_square.coord)
    if (index >= 0)
      Board(squares.updated(index, new_square))
    else
      this // Cell not found, return the current instance
  }

  def startPos(): Board = {
    val start_pos = this.squares.replace(Board.start_pos_pieces.map(_.toSquare()))
    Board(start_pos.toVector)
  }

  def makeMove(start_coord: Coord, end_coord: Coord): Board = {
    if (MoveValidator.isMoveValid(start_coord, end_coord, this)) {
      updateSquare(Square(end_coord, squares.find(_.coord == start_coord).get.piece))
        .updateSquare(Square(start_coord, Option.empty))
    } else {
      this
    }
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

  val start_pos_pieces = List(
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
