package htwg.se.chess.model
package boardComponent

import boardComponent.Coord
import boardComponent.pieces.{Piece, Pawn, Knight, Bishop, Rook, Queen, King}
import boardComponent.pieces.PieceColor._
import boardComponent.pieces.PieceColor
import boardComponent.pieces.PieceType._

import scala.util.{Try, Success, Failure}

enum Manned {
  case Own, Enemy, Free
}

case class MoveValidator(move: Move, board: Board) {

  private val start: Option[Piece] = board.squares(move.from)

  private val start_manned: Manned = {
    if (start.isEmpty) Manned.Free
    else if (start.map(_.color != board.turn) == Some(true)) Manned.Enemy
    else Manned.Own
  }

  private val full_path: List[Option[Piece]] = start.map(_.getPath(move.from, move.to).map(coord => board.squares(coord))).getOrElse(List()).reverse

  private val path: Try[List[Option[Piece]]] = Try(full_path.init)

  private val path_free: Try[Boolean] = path.map(_.forall(square => square.isEmpty))

  private val dest: Try[Option[Piece]] = Try(full_path.last)

  private val dest_manned: Option[Manned] = {
    if (dest.map(_.isEmpty) == Success(true)) Some(Manned.Free)
    else if (dest.map(_.map(_.color != board.turn).getOrElse(false)) == Success(true)) Some(Manned.Enemy)
    else if (dest.map(_.map(_.color == board.turn).getOrElse(false)) == Success(true)) Some(Manned.Own)
    else None
  }

  private val same_file: Boolean = move.from.file == move.to.file

  private val move_conceivable: Boolean = {
    (start, start_manned, path_free) match {
      case (Some(pawn: Pawn), Manned.Own, Success(true))  => (dest_manned == Some(Manned.Free) && same_file == true) || (dest_manned == Some(Manned.Enemy) && same_file == false)
      case (Some(other_piece), Manned.Own, Success(true)) => dest_manned == Some(Manned.Free) || dest_manned == Some(Manned.Enemy)
      case _                                              => false
    }
  }

  val resulting_promotion: Boolean = {
    start match {
      case Some(pawn: Pawn) if (pawn.color == WHITE) => move.from.rank == '7'
      case Some(pawn: Pawn) if (pawn.color == BLACK) => move.from.rank == '2'
      case _                                         => false
    }
  }


  val resulting_squares = (board.squares(move.from), resulting_promotion, move.from != move.to) match {
    case (Some(piece), true, true) => board.squares + (move.to -> Some(Piece(QUEEN, piece.color, promoted_on_move = Some(piece.move_count + 1))), move.from -> None)
    case (Some(piece), false, true) =>  board.squares + (move.to -> Some(piece.increaseMoveCount(1)), move.from -> None)
    case _ => board.squares
  }

  val resulting_captured: List[Piece] = List(board.squares(move.to)).collect { case Some(p: Piece) => p }

  def occupiedSquares(color_opt: Option[PieceColor] = None): Map[Coord, Piece] = color_opt match {
    case Some(color) => resulting_squares.collect { case (coord, Some(piece)) if (piece.color == color) => coord -> piece }
    case _           => resulting_squares.collect { case (coord, Some(piece)) => coord -> piece }
  }

  val occupied_squares_white   = occupiedSquares(Some(WHITE))
  val occupied_squares_black   = occupiedSquares(Some(BLACK))

  val resulting_king_white = occupied_squares_white.collectFirst{case (coord, piece: King) => coord}.get
  val resulting_king_black = occupied_squares_black.collectFirst{case (coord, piece: King) => coord}.get

  val resulting_check: Option[PieceColor] = {
    val sight_white: List[Coord] = occupied_squares_white.map((from, piece) => Pathing(from, piece, resulting_squares).withEnemyPieceThreats).flatten.toList
    val sight_black: List[Coord] = occupied_squares_black.map((from, piece) => Pathing(from, piece, resulting_squares).withEnemyPieceThreats).flatten.toList
    val black_checked            = sight_white.exists(_ == resulting_king_black)
    val white_checked            = sight_black.exists(_ == resulting_king_white)
    if (black_checked) Some(BLACK)
    else if (white_checked) Some(WHITE)
    else None
  }

  val resulting_move_notation: String = {
    start match {
      case Some(piece) => s"${piece.notation}${if(resulting_captured.size == 1) "x" else ""}${move.to.toString.toLowerCase}${if(resulting_check.isDefined) "+" else ""}"
      case _ => "unvalid"
    }
  }

  val move_valid: Boolean = {
    if (move_conceivable) {
      resulting_check match {
        case Some(color) => color != board.turn
        case None        => true
      }
    } else {
      false
    }
  }

}
