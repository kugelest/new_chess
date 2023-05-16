package htwg.se.chess
package model
package boardComponent
package boardBaseImpl

import boardBaseImpl.Coord
import boardBaseImpl.Coord.*
import boardBaseImpl.SquareExtensions._
import boardBaseImpl.SquareExtensions.Removable._
import boardBaseImpl.SquareExtensions.Addable._
import boardBaseImpl.SquareExtensions.Squareable._
import boardBaseImpl.pieces.{Piece, Pawn, Rook, Knight, Bishop, Queen, King}
import boardBaseImpl.pieces.PieceType.*
import util.PieceColor
import util.PieceColor.*

import scala.util.Try
import scala.util.Success
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._
import play.api.libs.json.{JsNumber, JsValue, Json, Writes}
import play.api.libs.json.Format

case class Board(squares: Vector[Square], capture_stack: List[Option[Square]], turn: PieceColor)
    extends BoardInterface {

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

  private def takeBackMove = move(-1) _
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

  override def toString(): String = {
    this.squares
      .sortBy(_.coord.print_ord)
      .map(_.piece.getOrElse("-"))
      .grouped(Coord.len)
      .map(_.mkString(" "))
      .mkString("\n")
  }

  // given Format[Board] = Json.format[Board]

  val cellWrites = new Writes[BoardInterface] {
    def writes(board: BoardInterface) = Json.obj(
      "value" -> "hello"
      // "given" -> cell.given,
      // "showCandidates" -> cell.showCandidates
    )
  }

  def toJson: JsValue = {
    Json.obj(
      "board" -> Json.obj(
        "turn" -> this.turn.toString(),
        "squares" -> Json.toJson(
          for {
            i <- 0 until squares.length
          } yield {
            Json.obj(
              "file" -> squares(i).coord.file.toString(),
              "rank" -> squares(i).coord.rank.toString(),
              "piece" -> squares(i).piece.map(_.getClass.toString.prepended('.').split("\\.").last)
            )
          }
        )
      )
    )
  }
}

object Board {
  // @Inject
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
}
