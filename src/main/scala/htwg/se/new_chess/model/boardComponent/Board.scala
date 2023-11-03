package htwg.se.chess.model
package boardComponent

import boardComponent.Coord
import boardComponent.Coord.*
// import boardComponent.SquareExtensions._
// import boardComponent.SquareExtensions.Removable._
// import boardComponent.SquareExtensions.Addable._
// import boardComponent.SquareExtensions.Squareable._
import boardComponent.pieces.{Piece, Pawn, Rook, Knight, Bishop, Queen, King}
import boardComponent.pieces.PieceColor
import boardComponent.pieces.PieceColor._

import scala.util.Try
import scala.util.Success
import scala.collection.immutable.Map

case class Board(squares: Map[Coord, Option[Piece[_]]], capture_stack: List[Option[Piece[_]]], turn: PieceColor) {

  def startPos(): Board = Board()
  def isMoveConceivable(from: Coord, to: Coord): Boolean = MoveValidator.isMoveConceivable(from, to, this)
  def isValid(): Boolean =  MoveValidator.isValid(this)

  private def forceMove(i: Int)(from: Coord, to: Coord): Board = {
    this.copy(
      squares = this.squares + (to -> this.squares(from).map(piece => piece.increaseMoveCount(i)), from -> None)
    )
  }

  private def forceMoveForward = forceMove(+1) _
  private def forceMoveBackward = forceMove(-1) _

  def doMove(from: Coord, to: Coord): Board = {
    val captured_piece = this.squares(to)
    val board = forceMoveForward(from, to)
    board.copy(capture_stack = captured_piece :: capture_stack, turn = nextTurn())
  }

  def undoMove(from: Coord, to: Coord): Board = {
    val board = forceMoveBackward(from, to)
    val new_board = board.capture_stack.match {
      case head :: tail => this.copy(squares = board.squares + (from -> head))
      case _            => board
    }
    new_board.copy(capture_stack = capture_stack.tail, turn = nextTurn())
  }

  def nextTurn(): PieceColor = {
    turn match {
      case WHITE => BLACK
      case BLACK => WHITE
    }
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

  private def advantage(l1: List[Piece[_]], l2: List[Piece[_]]): Int = {
    l2.map(_.worth).reduceOption(_+_).getOrElse(0) - l1.map(_.worth).reduceOption(_+_).getOrElse(0)
  }

  def advantage(): Int = {
    val (white_stack, black_stack) = this.captureStacks()
    this.advantage(white_stack, black_stack)
  }

  override def toString(): String = {
    val board = this.squares
      .toSeq
      .sortBy(_._1.print_ord)
      .map(_._2.getOrElse("-"))
      .grouped(Coord.len)
      .map(_.mkString(" "))
      .mkString("\n")
    val adv = "adv: " + this.advantage()
    val (w, b) = this.captureStacksStr()
    val stacks = w.mkString + "\n" + b.mkString
    board + "\n\n" + stacks + "\n\n" + adv
  }
}

object Board {
  def apply() = {
    new Board(
      Map(
        A1 -> Some(Rook(WHITE)),
        B1 -> Some(Knight(WHITE)),
        C1 -> Some(Bishop(WHITE)),
        D1 -> Some(Queen(WHITE)),
        E1 -> Some(King(WHITE)),
        F1 -> Some(Bishop(WHITE)),
        G1 -> Some(Knight(WHITE)),
        H1 -> Some(Rook(WHITE)),
        A2 -> Some(Pawn(WHITE)),
        B2 -> Some(Pawn(WHITE)),
        C2 -> Some(Pawn(WHITE)),
        D2 -> Some(Pawn(WHITE)),
        E2 -> Some(Pawn(WHITE)),
        F2 -> Some(Pawn(WHITE)),
        G2 -> Some(Pawn(WHITE)),
        H2 -> Some(Pawn(WHITE)),
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
        A7 -> Some(Pawn(BLACK)),
        B7 -> Some(Pawn(BLACK)),
        C7 -> Some(Pawn(BLACK)),
        D7 -> Some(Pawn(BLACK)),
        E7 -> Some(Pawn(BLACK)),
        F7 -> Some(Pawn(BLACK)),
        G7 -> Some(Pawn(BLACK)),
        H7 -> Some(Pawn(BLACK)),
        A8 -> Some(Rook(BLACK)),
        B8 -> Some(Knight(BLACK)),
        C8 -> Some(Bishop(BLACK)),
        D8 -> Some(Queen(BLACK)),
        E8 -> Some(King(BLACK)),
        F8 -> Some(Bishop(BLACK)),
        G8 -> Some(Knight(BLACK)),
        H8 -> Some(Rook(BLACK)),
      ), List(), WHITE
    )
  }
}


