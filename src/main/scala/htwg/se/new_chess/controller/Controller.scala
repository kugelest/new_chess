package htwg.se.chess
package controller

import util.Observable
import util.UndoManager
import util.Event
import model.boardComponent.Move
import model.boardComponent.Board
import model.boardComponent.Coord
import model.boardComponent.MoveValidator
import model.boardComponent.pieces.PieceColor

import scala.util.Try
import scala.util.Success

case class Controller(var board: Board) extends Observable {

  val undoManager = new UndoManager[Board]
  def doAndPublish(doThis: Move => Board, move: Move) = {
    board = doThis(move)
    PlayerState.next
    notifyObservers(Event.Move)
  }
  def doAndPublish(doThis: => Board) =
    board = doThis
    notifyObservers(Event.Move)
  def quit: Unit = notifyObservers(Event.Quit)
  def makeMove(move: Move): Board = undoManager.doStep(board, MoveCommand(move))
  // def get(x: Int, y: Int): String = board.get(x, y).toString
  def undo: Board = undoManager.undoStep(board)
  def redo: Board = undoManager.redoStep(board)
  override def toString: String = board.toString

}

object PlayerState:
  var piece_color = PieceColor.WHITE
  def player = piece_color.toString
  def next = if piece_color == PieceColor.WHITE then piece_color = PieceColor.BLACK else piece_color = PieceColor.WHITE
