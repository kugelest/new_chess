package htwg.se.chess.model
package boardComponent

import boardComponent.Coord
import boardComponent.pieces.{Piece, Pawn, Knight, Bishop, Rook, Queen, King}
import boardComponent.pieces.PieceColor._

object MoveValidator {

  def isMoveConceivable(from: Coord, to: Coord, board: Board): Boolean = {
    val piece = board.squares(from) match {
      case Some(piece) => piece
      case None        => return false
    }

    piece.color match {
      case board.turn =>
      case _          => return false
    }

    // path does not contain start and dest
    val (dest, path) =
      piece.getPath(from, to).collect{ case key if board.squares.contains(key) => board.squares(key) }.match {
        case head :: tail => (head, tail)
        case Nil          => return false
      }

    val path_empty = path.forall(square => square.isEmpty)
    val dest_empty = dest.isEmpty
    val dest_enemy = dest.map(_.color) != Option(piece.color)
    val same_file = from.file == to.file

    piece match {
      case _: Pawn =>
        (path_empty, dest_empty, dest_enemy, same_file) match {
          case (_, true, _, false)      => return false
          case (_, false, false, false) => return false
          case (_, false, _, true)      => return false
          case (false, _, _, _)         => return false
          case _                        =>

        }
      case _ =>
        (path_empty, dest_empty, dest_enemy) match {
          case (false, _, _)     => return false
          case (_, false, false) => return false
          case _                 =>
        }
    }

    true
  }

  def isValid(board: Board): Boolean = {
    val (squares_occupied_by_white, squares_occupied_by_black) = board.squares.collect{case (coord, piece_opt) if piece_opt.isDefined => (coord, piece_opt.get)}.partition(_._2.color == WHITE)
    val sight_white: List[Coord] = squares_occupied_by_white.map((coord, piece) => pieceSight(board, coord, piece)._2).flatten.toList
    val sight_black: List[Coord] = squares_occupied_by_black.map((coord, piece) => pieceSight(board, coord, piece)._2).flatten.toList
    val black_checked = sight_white.exists(_ == board.kingPos(BLACK))
    val white_checked = sight_black.exists(_ == board.kingPos(WHITE))
    val valid = board.turn.match {
      // colors are switched, because we are in the tmp_board and nextTurn() already happened
      case WHITE => !black_checked
      case BLACK => !white_checked
    }
    valid
  }

  private def pieceSight(board: Board, coord: Coord, piece: Piece): (Piece, List[Coord]) = {
    assert(board.squares.exists(_ == coord -> Some(piece)))

    val sight: (Piece, List[Coord]) = piece.match {
      case p: Pawn => (p, p.sightOnEmptyBoard(coord).flatten)
      case k: Knight => (k, k.sightOnEmptyBoard(coord).flatten)
      case k: King => (k, k.sightOnEmptyBoard(coord).flatten)
      case b: Bishop => {
        val si = b.sightOnEmptyBoard(coord).map(pathUntilPiece(board, _)).flatten
        (b, b.sightOnEmptyBoard(coord).map(pathUntilPiece(board, _)).flatten)
      }
      case r: Rook => (r, r.sightOnEmptyBoard(coord).map(pathUntilPiece(board, _)).flatten)
      case q: Queen => (q, q.sightOnEmptyBoard(coord).map(pathUntilPiece(board, _)).flatten)
      case x => (x, List())
    }
    sight
  }

  private def pathUntilPiece(board: Board, path: List[Coord]): List[Coord] = {
    val (empty_path, rest) = path.span(coord => board.squares(coord).isEmpty)
    val res = empty_path ++ rest.take(1)
    res
  }
}
