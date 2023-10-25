package htwg.se.chess
package controller

import util.Observable
import util.UndoManager
import util.Event
// import util.PieceColor
import model.BoardComponent.boardBaseImpl.pieces.PieceColor
import model.BoardComponent.BoardInterface
import model.BoardComponent.MoveInterface
import controller.MoveCommand

import scala.util.Try
import scala.util.Success
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._
import scala.util.Failure

case class Controller @Inject() (var board: BoardInterface) extends Observable {

  val undoManager = new UndoManager[BoardInterface]
  val injector = Guice.createInjector(new ChessModule)

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

  def whichTurn(): PieceColor = board.turn

  def undo: BoardInterface = undoManager.undoStep(board)
  def redo: BoardInterface = undoManager.redoStep(board)
  def quit: Unit = notifyObservers(Event.Quit)
  def save: Unit = {
    // notifyObservers()
  }

  def boardToHtml: String = {
    board.toHtml()
  }

  override def toString: String = board.toString

}
