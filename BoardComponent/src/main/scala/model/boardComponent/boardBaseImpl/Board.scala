package htwg.se.chess
package model
package boardComponent
package boardBaseImpl

// import boardComponent.Coord
import Coord._
import pieces.{Piece, Pawn, Rook, Knight, Bishop, Queen, King}
import pieces.PieceColor
import pieces.PieceColor._
import pieces.PieceType._

import scala.collection.immutable.Map
import play.api.libs.json._

case class Board(
    squares: Map[Coord, Option[Piece]],
    turn: PieceColor,
    in_check: Boolean,
    captured_pieces: List[Piece],
    moves: List[String]
) extends BoardInterface {

  def startPos: Board                      = Board()
  val whitePieces: List[Piece]             = occupiedSquares(Some(WHITE)).values.toList
  val blackPieces: List[Piece]             = occupiedSquares(Some(BLACK)).values.toList
  val whiteCapturedPieces: List[Piece]     = captured_pieces.filter(_.color == WHITE).sortBy(_.worth).reverse
  val blackCapturedPieces: List[Piece]     = captured_pieces.filter(_.color == BLACK).sortBy(_.worth).reverse
  val whiteCapturedPiecesStr: List[String] = blackCapturedPieces.map(_.toString)
  val blackCapturedPiecesStr: List[String] = whiteCapturedPieces.map(_.toString)
  val whiteKing: Coord                     = kingCoord(WHITE)
  val blackKing: Coord                     = kingCoord(BLACK)
  val checked: Option[PieceColor]          = if (in_check) Some(turn) else None
  val checkmate: Boolean                   = moveOptions.values.flatten.isEmpty
  val winner: Option[PieceColor]           = if (checkmate) Some(nextTurn) else None
  val advantage: Int                       = whitePieces.map(_.worth).reduce(_ + _) - blackPieces.map(_.worth).reduce(_ + _)

  def move(move: Move): Option[Board] = {
    val validator = MoveValidator(move, this)
    if (validator.move_valid)
      Some(
        this.copy(
          squares = validator.resulting_squares,
          turn = nextTurn,
          in_check = validator.resulting_check.isDefined,
          captured_pieces = this.captured_pieces ++ validator.resulting_captured,
          moves = this.moves :+ validator.resulting_move_notation
        )
      )
    else
      None
  }

  lazy val moveOptions: Map[Piece, List[Coord]] = {
    this
      .occupiedSquares(Some(this.turn))
      .map((from, piece) =>
        (
          piece,
          Pathing(from, piece, this.squares).moveOptions
            .filter(to => MoveValidator(Move(from, to), this).move_valid)
        )
      )
  }

  def moveOptions(from: Coord): List[Coord] = {
    this.squares(from) match {
      case Some(piece) if (piece.color == this.turn) => this.moveOptions(piece)
      case _                                         => Nil
    }
  }

  private def nextTurn: PieceColor = {
    this.turn match {
      case WHITE => BLACK
      case BLACK => WHITE
    }
  }

  private def kingCoord(color: PieceColor): Coord = {
    squares
      .find { case (coord, piece) =>
        piece.map(_.match { case King(`color`, _, _, _, _, _, _) => true; case _ => false }).getOrElse(false)
      }
      .map((coord, king) => coord)
      .get
  }

  def kingCheckedCoord: Option[Coord] = {
    this.checked match {
      case Some(color) => Some(this.kingCoord(color))
      case _           => None
    }
  }

  def occupiedSquares(color_opt: Option[PieceColor] = None): Map[Coord, Piece] = color_opt match {
    case Some(color) => this.squares.collect { case (coord, Some(piece)) if (piece.color == color) => coord -> piece }
    case _           => this.squares.collect { case (coord, Some(piece)) => coord -> piece }
  }

  def initBoardJson(): JsValue = {
    val squares = this.squares.keys.toSeq
      .sortBy(_.print_ord)
      .map(coord => (coord.toString.toLowerCase, coord.color.toString.toLowerCase))
      .toList
    Json.toJson(squares)
  }

  def squaresJson(): JsValue = {
    val squares = this.squares.toList
      .sortBy((coord, piece_opt) => coord.print_ord)
      .map((coord, piece_opt) =>
        Json.obj(
          "coord" -> coord.toString.toLowerCase,
          "color" -> coord.color.toString.toLowerCase,
          "piece" -> piece_opt.getOrElse("").toString
        )
      )
    Json.toJson(squares)
  }

  def gameInfoJson(): JsValue = {
    Json.obj(
      "turn"          -> this.turn.toString.toLowerCase,
      "winner"        -> this.winner.getOrElse("").toString.toLowerCase,
      "capture_stack" -> Json.obj("white" -> whiteCapturedPiecesStr, "black" -> blackCapturedPiecesStr),
      "advantage"     -> this.advantage,
      "undo_moves"    -> Json.toJson(this.moves)
    )
  }

  def toJson(): JsObject = {
    Json.obj(
      "board" -> Json.obj(
        "turn"          -> this.turn.toString.toLowerCase,
        "checked"       -> this.kingCheckedCoord.getOrElse("").toString.toLowerCase,
        "winner"        -> this.winner.getOrElse("").toString.toLowerCase,
        "capture_stack" -> Json.obj("white" -> whiteCapturedPiecesStr, "black" -> blackCapturedPiecesStr),
        "advantage"     -> this.advantage,
        "squares"       -> Json.toJson(
          this.squares.map((coord, piece_opt) =>
            coord.toString.toLowerCase -> Json.obj(
              "color" -> Json.toJson(coord.color.toString.toLowerCase),
              "piece" -> Json.toJson(piece_opt.getOrElse("").toString)
            )
          )
        ),
        "moves"         -> Json.toJson(this.moves)
      )
    )
  }

  def moveOptionsJson(from: Coord): JsValue = {
    Json.toJson(this.moveOptions(from).map(_.toString.toLowerCase))
  }

  override def toString(): String = {
    val board   = this.squares.toSeq
      .sortBy(_._1.print_ord)
      .map(_._2.getOrElse("-"))
      .grouped(Coord.len)
      .map(_.mkString(" "))
      .mkString("\n")
    val adv     = "adv: " + advantage
    val stacks  = "white_stack: " + whiteCapturedPiecesStr.mkString + "\n" + "black_stack: " + blackCapturedPiecesStr.mkString
    val checked = "checked: " + this.checked.getOrElse("").toString
    val winner  = "winner: " + this.winner.getOrElse("").toString
    val turn    = "turn: " + this.turn.toString
    val moves   = "moves: " + this.moves.mkString(" ")
    board + "\n" + turn + "\n" + stacks + "\n" + adv + "\n" + checked + "\n" + winner + "\n" + moves + "\n"
  }

}

object Board {
  def apply() = {
    new Board(
      Map(
        A1 -> Some(Piece(ROOK, WHITE)),
        B1 -> Some(Piece(KNIGHT, WHITE)),
        C1 -> Some(Piece(BISHOP, WHITE)),
        D1 -> Some(Piece(QUEEN, WHITE)),
        E1 -> Some(Piece(KING, WHITE)),
        F1 -> Some(Piece(BISHOP, WHITE)),
        G1 -> Some(Piece(KNIGHT, WHITE)),
        H1 -> Some(Piece(ROOK, WHITE)),
        A2 -> Some(Piece(PAWN, WHITE)),
        B2 -> Some(Piece(PAWN, WHITE)),
        C2 -> Some(Piece(PAWN, WHITE)),
        D2 -> Some(Piece(PAWN, WHITE)),
        E2 -> Some(Piece(PAWN, WHITE)),
        F2 -> Some(Piece(PAWN, WHITE)),
        G2 -> Some(Piece(PAWN, WHITE)),
        H2 -> Some(Piece(PAWN, WHITE)),
        A3 -> None,
        B3 -> None,
        C3 -> None,
        D3 -> None,
        E3 -> None,
        F3 -> None,
        G3 -> None,
        H3 -> None,
        A4 -> None,
        B4 -> None,
        C4 -> None,
        D4 -> None,
        E4 -> None,
        F4 -> None,
        G4 -> None,
        H4 -> None,
        A5 -> None,
        B5 -> None,
        C5 -> None,
        D5 -> None,
        E5 -> None,
        F5 -> None,
        G5 -> None,
        H5 -> None,
        A6 -> None,
        B6 -> None,
        C6 -> None,
        D6 -> None,
        E6 -> None,
        F6 -> None,
        G6 -> None,
        H6 -> None,
        A7 -> Some(Piece(PAWN, BLACK)),
        B7 -> Some(Piece(PAWN, BLACK)),
        C7 -> Some(Piece(PAWN, BLACK)),
        D7 -> Some(Piece(PAWN, BLACK)),
        E7 -> Some(Piece(PAWN, BLACK)),
        F7 -> Some(Piece(PAWN, BLACK)),
        G7 -> Some(Piece(PAWN, BLACK)),
        H7 -> Some(Piece(PAWN, BLACK)),
        A8 -> Some(Piece(ROOK, BLACK)),
        B8 -> Some(Piece(KNIGHT, BLACK)),
        C8 -> Some(Piece(BISHOP, BLACK)),
        D8 -> Some(Piece(QUEEN, BLACK)),
        E8 -> Some(Piece(KING, BLACK)),
        F8 -> Some(Piece(BISHOP, BLACK)),
        G8 -> Some(Piece(KNIGHT, BLACK)),
        H8 -> Some(Piece(ROOK, BLACK))
      ),
      WHITE,
      false,
      List(),
      List()
    )
  }
}
