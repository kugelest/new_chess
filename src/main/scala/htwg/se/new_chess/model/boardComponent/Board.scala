package htwg.se.chess.model
package boardComponent

import boardComponent.Coord
import boardComponent.Coord.*
import boardComponent.SquareExtensions._
import boardComponent.SquareExtensions.Removable._
import boardComponent.SquareExtensions.Addable._
import boardComponent.SquareExtensions.Squareable._
import boardComponent.pieces.{Piece, Pawn, Rook, Knight, Bishop, Queen, King}
import boardComponent.pieces.PieceType.*
import boardComponent.pieces.PieceColor.*

import scala.util.Try
import scala.util.Success

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

  def isMoveValid(from: String, to: String): Boolean = {
    val coords_try: List[Try[Coord]] = List(Try(Coord.fromStr(from)), Try(Coord.fromStr(to)))
    val coords = coords_try.collect { case Success(coord) => coord }
    val coords_are_valid = coords.length == 2
    val valid_move = coords_are_valid && MoveValidator.isMoveValid(coords(0), coords(1), this)
    valid_move
  }

  def makeMove(from: String, to: String): Board = {
    makeMove(Coord.fromStr(from), Coord.fromStr(to))
  }

  def makeMove(start_coord: Coord, end_coord: Coord): Board = {
    val start_square: Option[Square] = squares.find(_.coord == start_coord)
    val end_set = updateSquare(
      Square(end_coord, start_square.get.piece)
    )
    val end_set_and_start_removed = end_set.updateSquare(Square(start_coord, Option.empty))
    end_set_and_start_removed
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
    new Board(Coord.values.map(Square(_, Option.empty)).toVector)
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
