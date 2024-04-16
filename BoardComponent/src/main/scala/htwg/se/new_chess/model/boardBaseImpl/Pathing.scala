package htwg.se.chess.model
package BoardComponent
package boardBaseImpl

import pieces.Piece
import pieces._

import scala.util.{Try, Success, Failure}

case class Pathing(from: Coord, start_piece: Piece, squares: Map[Coord, Option[Piece]]) {

  def withPiece: List[Coord]      = withPieceThreats.concat(withPieceWalks).distinct
  def withOwnPiece: List[Coord]   = withOwnPieceThreats.concat(withOwnPieceWalks).distinct
  def withEnemyPiece: List[Coord] = withEnemyPieceThreats.concat(withEnemyPieceWalks).distinct
  def withoutPiece: List[Coord]   = withoutPieceThreats.concat(withoutPieceWalks).distinct

  def withPieceThreats: List[Coord]      = withPiece(threatening_sight_on_empty_board)
  def withOwnPieceThreats: List[Coord]   = withOwnPiece(threatening_sight_on_empty_board)
  def withEnemyPieceThreats: List[Coord] = withEnemyPiece(threatening_sight_on_empty_board)
  def withoutPieceThreats: List[Coord]   = withoutPiece(threatening_sight_on_empty_board)

  def withPieceWalks: List[Coord]      = withPiece(walking_sight_on_empty_board)
  def withOwnPieceWalks: List[Coord]   = withOwnPiece(walking_sight_on_empty_board)
  def withEnemyPieceWalks: List[Coord] = withEnemyPiece(walking_sight_on_empty_board)
  def withoutPieceWalks: List[Coord]   = withoutPiece(walking_sight_on_empty_board)

  def moveOptions: List[Coord] = {
    start_piece match {
      case _: Pawn                                              => withoutPieceWalks ++ withEnemyPieceThreats
      case _: Knight | _: Bishop | _: Rook | _: Queen | _: King => withEnemyPieceThreats
      case _                                                    => List()
    }
  }

  private def threatening_sight_on_empty_board: List[List[Coord]] = start_piece.threateningSightOnEmptyBoard(from)
  private def walking_sight_on_empty_board: List[List[Coord]]     = start_piece.walkingSightOnEmptyBoard(from)

  private def untilPiecePlusPiece(direction: List[Coord]): (List[Coord], Option[Coord]) = {
    val (until_piece, rest) = direction.span(coord => squares(coord).isEmpty)
    (until_piece, rest.headOption)
  }

  private def addPiece(until_piece: List[Coord], last_opt: Option[Coord]): List[Coord] = {
    last_opt match {
      case Some(last) =>
        squares(last) match {
          case Some(end_piece) => until_piece :+ last
          case _               => until_piece
        }
      case _          => until_piece
    }
  }

  private def addOwnPiece(until_piece: List[Coord], last_opt: Option[Coord]): List[Coord] = {
    last_opt match {
      case Some(last) =>
        squares(last) match {
          case Some(end_piece) if (end_piece.color == start_piece.color) => until_piece :+ last
          case _                                                         => until_piece
        }
      case _          => until_piece
    }
  }

  private def addEnemyPiece(until_piece: List[Coord], last_opt: Option[Coord]): List[Coord] = {
    last_opt match {
      case Some(last) =>
        squares(last) match {
          case Some(end_piece) if (end_piece.color != start_piece.color) => until_piece :+ last
          case _                                                         => until_piece
        }
      case _          => until_piece
    }
  }

  private def _withPiece: List[Coord] => List[Coord]      = untilPiecePlusPiece.andThen(addPiece)
  private def _withOwnPiece: List[Coord] => List[Coord]   = untilPiecePlusPiece.andThen(addOwnPiece)
  private def _withEnemyPiece: List[Coord] => List[Coord] = untilPiecePlusPiece.andThen(addEnemyPiece)

  private def withPiece(sight: List[List[Coord]]): List[Coord]      = sight.map(direction => _withPiece(direction)).flatten
  private def withOwnPiece(sight: List[List[Coord]]): List[Coord]   = sight.map(direction => _withOwnPiece(direction)).flatten
  private def withEnemyPiece(sight: List[List[Coord]]): List[Coord] = sight.map(direction => _withEnemyPiece(direction)).flatten
  private def withoutPiece(sight: List[List[Coord]]): List[Coord]   = sight.map(direction => untilPiecePlusPiece(direction)._1).flatten

}
