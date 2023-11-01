package htwg.se.chess
package controller

import util.Observable
import util.UndoManager
import util.Event
import model.boardComponent.Move
import model.boardComponent.Board
import model.boardComponent.Coord
import model.boardComponent.SquareColors
import model.boardComponent.MoveValidator
import model.boardComponent.pieces.PieceColor

import scala.util.Try
import scala.util.Success

case class Controller(var board: Board) extends Observable {

  val undoManager = new UndoManager[Board]

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
    board.startPos()
  }


  def makeMove(move: Move): Board = {
    if (board.isMoveValid(move.from, move.to))
      undoManager.doStep(board, MoveCommand(move))
    else
      undoManager.noStep(board, MoveCommand(move))
  }

  def whichTurn(): PieceColor = board.turn

  def captureStacks() = {
    val (whiteCaptureStack, blackCaptureStack) = board.captureStacks()
    (whiteCaptureStack, blackCaptureStack)
  }

  def undo: Board = undoManager.undoStep(board)
  def redo: Board = undoManager.redoStep(board)
  def quit: Unit = notifyObservers(Event.Quit)

  def squareData(): List[(String, String, String)] = {
    board.squares.sortBy(_.coord.print_ord).map(square => (square.coord.toString.toLowerCase, square.toString, square.coord.color.toString.toLowerCase)).toList
  }

  override def toString: String = board.toString

}
