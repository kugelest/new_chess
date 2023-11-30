package htwg.se.chess
package controller

import util.Observable
import util.UndoManager
import util.Event
import model.boardComponent.Move
import model.boardComponent.Board
import model.boardComponent.Coord
import model.boardComponent.SquareColor
import model.boardComponent.MoveValidator
import model.boardComponent.pieces.PieceColor
import model.boardComponent.pieces.PieceColor._

import scala.util.Try
import scala.util.Success
import play.api.libs.json._
// import play.api.libs.json.{JsValue, Json}

case class Controller(var board: Board) extends Observable {

  val undoManager = new UndoManager()

  def doAndPublish(doThis: Move => Board, move: Move) = {
    board = doThis(move)
    notifyObservers(Event.Move)
  }
  def doAndPublish(doThis: => Board) = {
    board = doThis
    notifyObservers(Event.Move)
  }

  def newGame: Board = {
    undoManager.clear()
    board.startPos
  }

  def makeMove(move: Move): Board = {
    this.board.move(move) match {
      case Some(board) => undoManager.doStep(board)
      case _ => undoManager.noStep(this.board)
    }
  }

  def kingCheckedCoord(): Option[Coord] = {
    board.checked match {
      case Some(WHITE) => Some(board.whiteKing)
      case Some(BLACK) => Some(board.blackKing)
      case _ => None
    }
  }

  def captureStacks() = (board.whiteCapturedPiecesStr, board.blackCapturedPiecesStr)

  def squareData(): List[(String, String, SquareColor)] = {
    board.squares.toSeq
      .sortBy(_._1.print_ord)
      .map((coord, piece_opt) => (coord.toString.toLowerCase, piece_opt.getOrElse("").toString, coord.color))
      .toList
  }

  def squareDataStr(): List[(String, String, String)] = {
    board.squares.toSeq
      .sortBy(_._1.print_ord)
      .map((coord, piece_opt) => (coord.toString.toLowerCase, piece_opt.getOrElse("").toString, coord.color.toString.toLowerCase))
      .toList
  }


  def boardJson(): JsValue = board.toJson()
  def moveOptionsJson(from: Coord): JsValue = board.moveOptionsJson(from)

  def turn(): PieceColor = board.turn
  def advantage(): Int = board.advantage
  def winner(): Option[PieceColor] = board.winner

  def undo: Board = undoManager.undoStep(board)
  def redo: Board = undoManager.redoStep(board)
  def quit: Unit = notifyObservers(Event.Quit)

  override def toString: String = board.toString

}
