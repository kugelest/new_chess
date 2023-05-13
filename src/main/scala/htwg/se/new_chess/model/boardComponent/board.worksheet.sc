import htwg.se.new_chess.model.boardComponent.Piece
import htwg.se.new_chess.model.boardComponent.Coord
import htwg.se.new_chess.model.boardComponent.Coord.*
import htwg.se.new_chess.model.boardComponent.Board
import htwg.se.new_chess.model.boardComponent.Square
import htwg.se.new_chess.model.boardComponent.MoveValidator
import htwg.se.new_chess.model.boardComponent.pieces.{Pawn, Rook, Knight, Bishop, Queen, King}
// import htwg.se.new_chess.model.boardComponent.SquareExtensions._
import htwg.se.new_chess.model.boardComponent.SquareExtensions.Removable._
import htwg.se.new_chess.model.boardComponent.SquareExtensions.Addable._
import htwg.se.new_chess.model.boardComponent.SquareExtensions.Squareable._
import htwg.se.new_chess.model.boardComponent.PieceType.*
import htwg.se.new_chess.model.boardComponent.PieceColor.*

val coord: Coord = A5
val board: Board = Board().startPos()
// val square: board.squares
val validator: MoveValidator = MoveValidator(board)
validator.isMoveValid(A1, A2)

A1.upperNeighbors()
A1.upperNeighbors().reverse.dropWhile(_ != A4)
A1.rightNeighbor()
val knightmoves = D4.knightNeighbors()
knightmoves.find(_ == B5)
knightmoves.find(_ == B5)

val piece = Piece(KING, WHITE)

val square_with_piece = Square(A1, Option(piece))
val square_opt_with_piece = Option(Square(A1, Option(piece)))
val square_without_piece = Square(A1, None)
val square_opt_without_piece = Option(Square(A1, None))
square_with_piece.piece.map(_.color)
square_opt_with_piece.map(_.piece.isEmpty)
square_opt_without_piece.map(_.piece.isEmpty)

val hello: List[Option[Int]] = List(Option.empty, Option(1))

hello.map(_.map(_ + 1))
