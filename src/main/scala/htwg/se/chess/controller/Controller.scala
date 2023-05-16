package htwg.se.chess
package controller

import util.Observable
import util.UndoManager
import util.Event
import util.PieceColor
import model.boardComponent.BoardInterface
import model.boardComponent.MoveInterface
import model.fileIoComponent.FileIOInterface
import controller.MoveCommand

import scala.util.Try
import scala.util.Success
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._
import scala.util.Failure

case class Controller @Inject() (var board: BoardInterface) extends Observable {

  val undoManager = new UndoManager[BoardInterface]
  val injector = Guice.createInjector(new ChessModule)
  val fileIO = injector.instance[FileIOInterface]

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
    fileIO.save(board)
    // notifyObservers()
  }
  def load: BoardInterface = {
    fileIO.load match {
      case Success(value) => value.get
      case Failure(exception) =>
        println(s"Failure: ${exception.getMessage}"); board
    }
    // val gridOptionResult = fileIo.load

    // gridOptionResult match {
    //   case Success(gridOption) =>
    //     gridOption match {
    //       case Some(_grid) =>
    //         grid = _grid
    //         gameStatus = LOADED
    //       case None =>
    //         createEmptyGrid
    //         gameStatus = COULD_NOT_LOAD
    //     }
    //   case Failure(e) =>
    //     logger.error(
    //       "Error occured while loading game: " + e.getMessage)
    //     createEmptyGrid
    //     gameStatus = COULD_NOT_LOAD
    // }

    // (new CellChanged)
  }

  override def toString: String = board.toString

}
