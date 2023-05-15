package htwg.se.chess
package controller

import util.Observable
import util.UndoManager
import util.Event
import model.boardComponent.BoardInterface
import model.boardComponent.MoveInterface
import model.boardComponent.PieceColorInterface
import controller.MoveCommand

import scala.util.Try
import scala.util.Success

case class Controller(var board: BoardInterface) extends Observable {

  val undoManager = new UndoManager[BoardInterface]

  def doAndPublish(doThis: MoveInterface => BoardInterface, move: MoveInterface) = {
    board = doThis(move)
    notifyObservers(Event.Move)
  }
  def doAndPublish(doThis: => BoardInterface) = {
    board = doThis
    notifyObservers(Event.Move)
  }

  def newGame: BoardInterface = {
    undoManager.clear()
    board.startPos()
  }

  def makeMove(move: MoveInterface): BoardInterface = {
    if (board.isMoveValid(move.from, move.to))
      undoManager.doStep(board, MoveCommand(move))
    else
      undoManager.noStep(board, MoveCommand(move))
  }

  def whichTurn(): PieceColorInterface = board.turn

  def undo: BoardInterface = undoManager.undoStep(board)
  def redo: BoardInterface = undoManager.redoStep(board)
  def quit: Unit = notifyObservers(Event.Quit)

  override def toString: String = board.toString

}
