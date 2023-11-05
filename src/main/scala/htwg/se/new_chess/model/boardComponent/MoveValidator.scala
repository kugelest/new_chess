package htwg.se.chess.model
package boardComponent

import boardComponent.Coord
import boardComponent.pieces.{Piece, Pawn, Knight, Bishop, Rook, Queen, King}
import boardComponent.pieces.PieceColor._
import htwg.se.chess.model.boardComponent.pieces.PieceColor

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

  def isValid(board: Board): (Boolean, Boolean) = {
    val (squares_occupied_by_white, squares_occupied_by_black) = occupiedSquares(board)
    val sight_white: List[Coord] = squares_occupied_by_white.map((coord, piece) => pieceSightSeeingOwnPieces(board, coord, piece)._2).flatten.toList
    val sight_black: List[Coord] = squares_occupied_by_black.map((coord, piece) => pieceSightSeeingOwnPieces(board, coord, piece)._2).flatten.toList
    val black_checked = sight_white.exists(_ == board.kingCoord(BLACK))
    val white_checked = sight_black.exists(_ == board.kingCoord(WHITE))
    val (valid, check) = board.turn.match {
      case WHITE => (!white_checked, black_checked)
      case BLACK => (!black_checked, white_checked)
    }
    (valid, check)
  }

  def moveOptions(board: Board, from: Coord, piece: Piece): List[Coord] = {
    val all_move_options = moveOptions(board, piece.color)
    all_move_options(piece)
  }

  def moveOptions(board: Board, color: PieceColor): Map[Piece, List[Coord]] = {
    val (squares_occupied_by_white, squares_occupied_by_black) = occupiedSquares(board)
    val move_options_white: Map[Piece, List[Coord]] = squares_occupied_by_white.map((coord, piece) => pieceSightNotSeeingOwnPieces(board, coord, piece))
    val move_options_black: Map[Piece, List[Coord]] = squares_occupied_by_black.map((coord, piece) => pieceSightNotSeeingOwnPieces(board, coord, piece))
    val move_options = color.match {
      case WHITE => move_options_white
      case BLACK => move_options_black
    }
    move_options
  }

  private def pieceSightSeeingOwnPieces = pieceSight(pathSeeingOwnPiece)(false) _
  private def pieceSightNotSeeingOwnPieces = pieceSight(pathNotSeeingOwnPiece)(true) _

  private def pieceSight(f: (Board, List[Coord], PieceColor) => List[Coord])(including_start: Boolean)(board: Board, coord: Coord, piece: Piece): (Piece, List[Coord]) = {
    assert(board.squares.exists(_ == coord -> Some(piece)))
    val sight: (Piece, List[Coord]) = piece.match {
      case p: Pawn => (p, p.sightOnEmptyBoard(coord).flatten)
      case k: Knight => (k, k.sightOnEmptyBoard(coord).flatten)
      case k: King => (k, k.sightOnEmptyBoard(coord).flatten)
      case b: Bishop => (b, b.sightOnEmptyBoard(coord).map(path_empty_board => f(board, path_empty_board, b.color)).flatten)
      case r: Rook => (r, r.sightOnEmptyBoard(coord).map(path_empty_board => f(board, path_empty_board, r.color)).flatten)
      case q: Queen => (q, q.sightOnEmptyBoard(coord).map(path_empty_board => f(board, path_empty_board, q.color)).flatten)
      case x => (x, List())
    }
    including_start.match {
      case true => (sight._1, coord :: sight._2)
      case _ => sight
    }
  }

  private def pathSeeingOwnPiece = pathToPiece(true) _
  private def pathNotSeeingOwnPiece = pathToPiece(false) _

  private def pathToPiece(seeing_own: Boolean)(board: Board, path: List[Coord], piece_color: PieceColor): List[Coord] = {
    val (empty_path, rest) = path.span(coord => board.squares(coord).isEmpty)
    val first_piece_coord: Option[Coord] = rest.take(1).lift(0)
    val path_til_piece = (seeing_own, first_piece_coord).match {
      case (true, fpc) => empty_path ++ fpc.toList
      case (false, Some(fpc)) if board.squares(fpc).map(_.color != piece_color).get => empty_path :+ fpc
      case _ => empty_path
    }
    path_til_piece
  }


  private def occupiedSquares(board: Board): (Map[Coord, Piece], Map[Coord, Piece]) = {
    board.squares.collect{case (coord, piece_opt) if piece_opt.isDefined => (coord, piece_opt.get)}.partition(_._2.color == WHITE)
  }
}
