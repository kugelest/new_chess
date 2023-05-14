// package htwg.se.chess.model.

// import pieces.Piece
import htwg.se.chess.model.boardComponent.Coord
import htwg.se.chess.model.boardComponent.Coord.*
import htwg.se.chess.model.boardComponent.Board
import htwg.se.chess.model.boardComponent.Square
import htwg.se.chess.model.boardComponent.MoveValidator
import htwg.se.chess.model.boardComponent.pieces.{Piece, Pawn, Rook, Knight, Bishop, Queen, King}
// import htwg.se.new_chess.model.boardComponent.SquareExtensions._
import htwg.se.chess.model.boardComponent.SquareExtensions.Removable._
import htwg.se.chess.model.boardComponent.SquareExtensions.Addable._
import htwg.se.chess.model.boardComponent.SquareExtensions.Squareable._
import htwg.se.chess.model.boardComponent.pieces.PieceType.*
import htwg.se.chess.model.boardComponent.pieces.PieceColor.*

val coord: Coord = A5
val board: Board = Board().startPos()
// val square: board.squares
MoveValidator.isMoveValid(A1, A2, board)

A1.upperNeighbors()
A1.upperNeighbors().reverse.dropWhile(_ != A4)
A1.rightNeighbor()
val knightmoves = D4.knightNeighbors()
knightmoves.find(_ == B5)
knightmoves.find(_ == B5)

// val piece = Piece(KING, WHITE, true)
val piece =
  Piece(KING, WHITE, true)

piece.copy(unmoved = false).unmoved

val square_with_piece = Square(A1, Option(piece))
val square_opt_with_piece = Option(Square(A1, Option(piece)))
val square_without_piece = Square(A1, None)
val square_opt_without_piece = Option(Square(A1, None))
square_with_piece.piece.map(_.color)
square_opt_with_piece.map(_.piece.isEmpty)
square_opt_without_piece.map(_.piece.isEmpty)

val hello: List[Option[Int]] = List(Option.empty, Option(1))

hello.map(_.map(_ + 1))
