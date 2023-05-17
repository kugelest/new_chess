// package htwg.se.chess.model.

// import pieces.Piece
import htwg.se.chess.model.BoardComponent.boardBaseImpl.Coord
import htwg.se.chess.model.BoardComponent.boardBaseImpl.Coord.*
// import htwg.se.chess.model.boardComponent.Coord.*
// import htwg.se.chess.model.boardComponent.Board
// import htwg.se.chess.model.boardComponent.Square
// import htwg.se.chess.model.boardComponent.MoveValidator
// import htwg.se.chess.model.boardComponent.pieces.{Piece, Pawn, Rook, Knight, Bishop, Queen, King}
// import htwg.se.new_chess.model.boardComponent.SquareExtensions._
// import htwg.se.chess.model.boardComponent.SquareExtensions.Removable._
// import htwg.se.chess.model.boardComponent.SquareExtensions.Addable._
// import htwg.se.chess.model.boardComponent.SquareExtensions.Squareable._
// import htwg.se.chess.model.boardComponent.pieces.PieceType.*
// import htwg.se.chess.model.boardComponent.pieces.PieceColor.*

val coord = A4

coord.file
coord.rank
coord.ordinal
coord.print_ord
coord.color
coord.upperTwo()
coord.rightNeighbor()
coord.upperRightNeighbor()
coord.surroundingNeighbors()