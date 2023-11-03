package htwg.se.chess.model
package boardComponent

import boardComponent.Coord
import boardComponent.pieces.Pawn

object MoveValidator {

  def isMoveConceivable(from: Coord, to: Coord, board: Board): Boolean = {
    val piece = board.squares.get(from).get match {
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
    true
  }
}
