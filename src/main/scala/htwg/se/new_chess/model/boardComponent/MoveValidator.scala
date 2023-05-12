package htwg.se.new_chess.model.boardComponent

import htwg.se.new_chess.model.boardComponent.Coord

class MoveValidator(board: Board) {

  def isMoveValid(start_coord: Coord, end_coord: Coord): Boolean = {
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
    val (dest, path) =
      piece.getPath(start_coord, end_coord).map(path_coord => board.squares.find(_.coord == path_coord)).reverse match {
        case head :: tail => (head, tail)
        case Nil          => return false
      }
    val path_is_free = !path.exists(square => !square.get.piece.isEmpty)
    val dest_empty_or_enemy = dest.get.piece.get.color != start_square.piece.get.color

    if (!path_is_free || !dest_empty_or_enemy) return false

    true
  }
}
