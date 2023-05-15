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
  def doAndPublish(doThis: => Board) = {
    board = doThis
    notifyObservers(Event.Move)
  }

  // def checkMove(move: Move)
  def makeMove(move: Move): Board = {
    if (board.isMoveValid(move.from, move.to))
      undoManager.doStep(board, MoveCommand(move))
    else
      undoManager.noStep(board, MoveCommand(move))
  }
  def undo: Board = undoManager.undoStep(board)
  def redo: Board = undoManager.redoStep(board)
  def quit: Unit = notifyObservers(Event.Quit)

  override def toString: String = board.toString

  object PlayerState {
    var piece_color = PieceColor.WHITE
    def player = piece_color.toString
    def next = {
      if (piece_color == PieceColor.WHITE)
        piece_color = PieceColor.BLACK
      else
        piece_color = PieceColor.WHITE
    }
  }
}
