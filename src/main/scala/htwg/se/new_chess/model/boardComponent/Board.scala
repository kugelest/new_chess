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
import boardComponent.pieces.PieceColor
import boardComponent.pieces.PieceColor.*

import scala.util.Try
import scala.util.Success

case class Board(squares: Vector[Square], capture_stack: List[Option[Square]], turn: PieceColor) {

  def updateSquare(new_square: Square): Board = {
    val index = squares.indexWhere(_.coord == new_square.coord)
    if (index >= 0)
      this.copy(squares = squares.updated(index, new_square))
    else
      this
  }

  private def initBoard(): Board = Board()

  def startPos(): Board = {
    val fresh_board = initBoard()
    fresh_board.copy(squares = fresh_board.squares.replace(Board.start_pos_pieces.map(_.toSquare())).toVector)
  }

  def isMoveValid(from: String, to: String): Boolean = {
    val coords_try: List[Try[Coord]] = List(Try(Coord.fromStr(from)), Try(Coord.fromStr(to)))
    val coords = coords_try.collect { case Success(coord) => coord }
    val coords_are_valid = coords.length == 2
    val valid_move = coords_are_valid && MoveValidator.isMoveValid(coords(0), coords(1), this)
    valid_move
  }

  private def move(n: Int)(start_coord: Coord, end_coord: Coord): Board = {
    val start_square: Option[Square] = squares.find(_.coord == start_coord)
    val end_set = updateSquare(
      Square(
        end_coord,
        start_square.map(s => s.copy(piece = s.piece.map(p => p.copy(move_count = p.move_count + n)))).get.piece
      )
    )
    val end_set_and_start_removed = end_set.updateSquare(Square(start_coord, Option.empty))
    end_set_and_start_removed
  }

  private def makeMove = move(+1) _
  private def takeBackMove = move(-1) _

  def doMove(from: String, to: String): Board = {
    val start_coord = Coord.fromStr(from)
    val end_coord = Coord.fromStr(to)
    val end_square_opt = this.squares.find(_.coord == end_coord)
    val capture_square = end_square_opt match {
      case Some(square) if square.piece.isDefined => Option(square)
      case _                                      => Option.empty
    }

    val board = makeMove(start_coord, end_coord)
    board.copy(capture_stack = capture_square :: capture_stack, turn = nextTurn())
  }

  // def takeBackMove(from: String, to: String): Board = takeBackMove(Coord.fromStr(from), Coord.fromStr(to))

  def undoMove(from: String, to: String): Board = {
    val start_coord = Coord.fromStr(from)
    val end_coord = Coord.fromStr(to)
    val board = takeBackMove(start_coord, end_coord)
    val new_board = board.capture_stack match {
      case head :: tail =>
        head match {
          case Some(square) => board.updateSquare(square)
          case _            => board
        }
      case _ => board
    }
    new_board.copy(capture_stack = capture_stack.tail, turn = nextTurn())
  }

  def nextTurn(): PieceColor = {
    turn match {
      case WHITE => BLACK
      case BLACK => WHITE
    }
  }

  def captureStacks() = {
    val captured_squares = this.capture_stack.flatten
    val captured_pieces = captured_squares.map(_.piece).flatten
    val (whiteStack, blackStack) = captured_pieces.partition {
      case piece if piece.color == PieceColor.WHITE => true
      case piece if piece.color == PieceColor.BLACK => false
    }
    (whiteStack, blackStack)
  }

  def captureStacksStr() = {
    val (whiteCaptureStack, blackCaptureStack) = this.captureStacks()
    (whiteCaptureStack.map(_.toString), blackCaptureStack.map(_.toString))
  }

  def advantage(): Int = {
    val (white_stack, black_stack) = this.captureStacks()
    Board.advantage(white_stack, black_stack)
  }

  override def toString(): String = {
    val board = this.squares
      .sortBy(_.coord.print_ord)
      .map(_.piece.getOrElse("-"))
      .grouped(Coord.len)
      .map(_.mkString(" "))
      .mkString("\n")
    val adv = "adv: " + this.advantage()
    val (w, b) = this.captureStacksStr()
    val stacks = w.mkString + "\n" + b.mkString
    board + "\n\n" + stacks + "\n\n" + adv
  }
}

object Board {
  def apply() = {
    new Board(Coord.values.map(Square(_, Option.empty)).toVector, List(), WHITE)
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

  private def advantage(l1: List[Piece], l2: List[Piece]): Int = {
    l2.map(_.worth).reduceOption(_+_).getOrElse(0) - l1.map(_.worth).reduceOption(_+_).getOrElse(0)
  }
}
