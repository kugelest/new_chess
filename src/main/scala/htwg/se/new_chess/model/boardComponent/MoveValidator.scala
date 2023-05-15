package htwg.se.chess.model
package boardComponent

import boardComponent.Coord
import boardComponent.pieces.Pawn

object MoveValidator {

  def isMoveValid(start_coord: Coord, end_coord: Coord, board: Board): Boolean = {
    val start_square = board.squares.find(_.coord == start_coord) match {
      case Some(square) => square
      case None         => return false
    }
    val end_square = board.squares.find(_.coord == end_coord) match {
      case Some(square) => square
      case None         => return false
    }
    val piece = start_square.piece match {
      case Some(piece) => piece
      case None        => return false
    }

    // path does not contain start and dest
    val (dest, path) =
      piece.getPath(start_coord, end_coord).map(path_coord => board.squares.find(_.coord == path_coord).get) match {
        case head :: tail => (head, tail)
        case Nil          => return false
      }

    val path_empty = !path.exists(square => !square.piece.isEmpty)
    val dest_empty = dest.piece.isEmpty
    val dest_enemy = dest.piece.map(_.color) != Option(piece.color)
    val same_file = start_square.coord.file == dest.coord.file

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
}
