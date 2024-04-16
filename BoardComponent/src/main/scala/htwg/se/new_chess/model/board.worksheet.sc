// package htwg.se.chess.model
// package model.BoardComponent.boardBaseImpl
//
// val a = Cord(0)
//
// // import pieces.Piece
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.Coord
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.Coord.*
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.Board
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.Square
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.MoveValidator
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.pieces.{Piece, Pawn, Rook, Knight, Bishop, Queen, King}
// // import htwg.se.new_chess.model.BoardComponent.boardBaseImpl.SquareExtensions._
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.SquareExtensions.Removable._
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.SquareExtensions.Addable._
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.SquareExtensions.Squareable._
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.pieces.PieceType.*
// import htwg.se.chess.model.BoardComponent.boardBaseImpl.pieces.PieceColor.*
//
// val coord: Coord = A5
// val board: Board = Board().startPos()
// // val square: board.squares
// val piece = Piece(KING, WHITE, true)
// val square_with_piece = Square(A1, Option(piece))
//
// MoveValidator.isMoveValid(A1, A2, board)
//
// board.copy(squares = Vector(square_with_piece))
//
// A1.upperNeighbors()
// A1.upperNeighbors().reverse.dropWhile(_ != A4)
// A1.rightNeighbor()
// val knightmoves = D4.knightNeighbors()
// knightmoves.find(_ == B5)
// knightmoves.find(_ == B5)
//
// // val piece = Piece(KING, WHITE, true)
//
// piece.copy(move_count = 1)
//
// val square_opt_with_piece = Option(Square(A1, Option(piece)))
// val square_without_piece = Square(A1, None)
// val square_opt_without_piece = Option(Square(A1, None))
// square_with_piece.piece.map(_.color)
// square_opt_with_piece.map(_.piece.isEmpty)
// square_opt_without_piece.map(_.piece.isEmpty)
//
// val hello: List[Option[Int]] = List(Option.empty, Option(1))
//
// hello.map(_.map(_ + 1))
