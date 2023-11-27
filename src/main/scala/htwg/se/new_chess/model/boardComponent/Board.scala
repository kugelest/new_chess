package htwg.se.chess.model
package boardComponent

import boardComponent.Coord._
import boardComponent.pieces.{Piece, Pawn, Rook, Knight, Bishop, Queen, King}
import boardComponent.pieces.PieceColor
import boardComponent.pieces.PieceColor._
import boardComponent.pieces.PieceType._

import scala.collection.immutable.Map
import scala.collection.immutable.TreeMap
import play.api.libs.json._
// import play.api.libs.json.{JsValue, Json}

case class Board(
    squares: Map[Coord, Option[Piece]],
    capture_stack: List[Option[Piece]],
    check_stack: List[Boolean],
    turn: PieceColor,
    checkmate: Option[PieceColor]
) {

  def startPos(): Board = Board()
  def isMoveConceivable(from: Coord, to: Coord): Boolean = MoveValidator.isMoveConceivable(from, to, this)
  def isValid(): (Boolean, Boolean) = MoveValidator.isValid(this)

  def checkCheckmate(): Option[PieceColor] = {
    var board_tmp = this
    val move_options = MoveValidator.moveOptions(this, this.turn).values
    val not_possible = move_options.map { moves =>
      moves.match {
        case from :: to_options =>
          to_options.forall { to =>
            {
              if (MoveValidator.isMoveConceivable(from, to, this)) {
                board_tmp = this.copy().doMove(from, to, false).copy(turn = this.turn)
                val (valid, _) = MoveValidator.isValid(board_tmp)
                !valid
              } else {
                true
              }
            }
          }
        case _ => true
      }
    }
    if (!not_possible.toList.contains(false)) Some(this.turn) else None
  }

  def doMove(from: Coord, to: Coord, checks: Boolean): Board = {
    val captured_square = this.squares(to)
    val piece_opt = this.squares(from)
    val promotion = piece_opt.match {
      case Some(piece) if piece.isInstanceOf[Pawn] => MoveValidator.promotion(this, from, piece.asInstanceOf[Pawn])
      case _ => None
    }
    val board = forceMovementForward(from, to, promotion)
    board.copy(capture_stack = captured_square :: capture_stack, check_stack = checks :: check_stack, turn = nextTurn())
  }

  def undoMove(from: Coord, to: Coord): Board = {
    val board = forceMovementBackward(from, to, None)
    val new_board = board.capture_stack.match {
      case head :: tail => this.copy(squares = board.squares + (from -> head), checkmate = None)
      case _            => board
    }
    new_board.copy(capture_stack = capture_stack.tail, check_stack = check_stack.tail, turn = nextTurn())
  }

  def nextTurn(): PieceColor = {
    turn match {
      case WHITE => BLACK
      case BLACK => WHITE
    }
  }

  def winner(): Option[PieceColor] = {
    this.checkmate.match {
      case Some(BLACK) => Some(WHITE)
      case Some(WHITE) => Some(BLACK)
      case _ => None
    }
  }

  def kingCoord(color: PieceColor): Coord = {
    squares
      .find { case (coord, piece) =>
        piece.map(_.match { case King(`color`, _, _, _, _, _, _) => true; case _ => false }).getOrElse(false)
      }
      .map((coord, king) => coord)
      .get
  }

  def kingChecked(): Option[PieceColor] = {
    this.check_stack.lift(0) match {
      case Some(checked) if checked => Some(this.turn)
      case _                        => None
    }
  }

  def moveOptions(from: Coord): List[Coord] = {
    this.squares(from).match {
      case Some(piece) => MoveValidator.moveOptions(this, from, piece)
      case _ => List(from)
    }
  }

  def moveOptionsJson(from: Coord): JsValue = {
    Json.toJson(this.moveOptions(from).map(_.toString.toLowerCase))
  }

  def captureStacks() = {
    val captured_pieces = this.capture_stack.flatten
    val (whiteStack, blackStack) = captured_pieces.partition {
      case piece if piece.color == PieceColor.WHITE => true
      case piece if piece.color == PieceColor.BLACK => false
    }
    (whiteStack, blackStack)
  }

  def captureStacksStr() = {
    val (whiteCaptureStack, blackCaptureStack) = this.captureStacks()
    (whiteCaptureStack.map(_.toString), blackCaptureStack.map(_.toString))
  }

  def advantage(): Int = {
    val white_worth = whitePieces().map(_.worth).reduce(_+_)
    val black_worth = blackPieces().map(_.worth).reduce(_+_)
    white_worth - black_worth
  }

  def kingCheckedCoord(): Option[Coord] = {
    this.kingChecked().match {
      case Some(color) => Some(this.kingCoord(color))
      case _ => None
    }
  }

  private def whitePieces = pieces(WHITE) _
  private def blackPieces = pieces(BLACK) _
  private def pieces(color: PieceColor)(): List[Piece] = {
    this.squares.values.collect{ case Some(piece) if(piece.color == color) => piece }.toList
  }

  private def pieces(): Map[Coord, Piece] = this.squares.collect{ case (coord, Some(piece)) => coord -> piece }

  private def forceMovementForward = forceMovement(+1) _
  private def forceMovementBackward = forceMovement(-1) _
  private def forceMovement(i: Int)(from: Coord, to: Coord, promotion_opt: Option[PieceColor]): Board = {
    val piece_opt = this.squares(from)
    (piece_opt, promotion_opt).match {
      case (Some(piece), Some(color)) if(i > 0) => this.copy( squares = this.squares + (to -> Some(Piece(QUEEN, color, promoted_on_move = Some(piece.move_count + i))), from -> None))
      case (Some(queen: Queen), None) if(i < 0 && Some(queen.move_count) == queen.promoted_on_move) =>
        this.copy( squares = this.squares + (to -> Some(Piece(PAWN, queen.color, move_count = queen.move_count + i)), from -> None))
      case _ => this.copy( squares = this.squares + (to -> this.squares(from).map(piece => piece.increaseMoveCount(i)), from -> None))
    }
  }

  override def toString(): String = {
    val board = this.squares.toSeq
      .sortBy(_._1.print_ord)
      .map(_._2.getOrElse("-"))
      .grouped(Coord.len)
      .map(_.mkString(" "))
      .mkString("\n")
    val adv = "adv: " + this.advantage()
    val (w, b) = this.captureStacksStr()
    val stacks = "white_stack: " + w.mkString + "\n" + "black_stack: " + b.mkString
    val checked = "checked: " + this.kingChecked().getOrElse("").toString
    val checkmate = "checkmated: " + this.checkmate.getOrElse("").toString
    val move_options = "options_f3: " + this.moveOptions(F3).mkString(", ")
    board + "\n" + stacks + "\n" + adv + "\n" + checked + "\n" + checkmate + "\n" + move_options
  }

  def toJson(): JsValue = {
    val (w, b) = this.captureStacksStr()
    Json.obj(
      "board" -> Json.obj(
        "turn" -> this.turn.toString.toLowerCase,
        "checked" -> this.kingCheckedCoord().getOrElse("").toString.toLowerCase,
        "winner" -> this.winner().getOrElse("").toString.toLowerCase,
        "capture_stack" -> Json.obj("white" -> w.mkString, "black" -> b.mkString),
        "advantage" -> this.advantage(),
        "squares" -> Json.toJson(
          this.squares.map((coord, piece_opt) =>
            coord.toString.toLowerCase -> Json.obj(
              "color" -> Json.toJson(coord.color.toString.toLowerCase),
              "piece" -> Json.toJson(piece_opt.getOrElse("").toString),
            )
          )
        )
      )
    )
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
      List(),
      List(),
      WHITE,
      None
    )
  }
}
