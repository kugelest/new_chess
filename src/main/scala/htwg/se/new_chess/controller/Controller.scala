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
import model.boardComponent.pieces.PieceColor._

import scala.util.Try
import scala.util.Success

case class Controller(var board: Board) extends Observable {

  val undoManager = new UndoManager[Board]
  var board_tmp = board

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
    if (board.isMoveConceivable(move.from, move.to)) {
      board_tmp = board.doMove(move.from, move.to, false).copy(turn = board.turn)
      val (valid, checks) = board_tmp.isValid()
      val checkmate = if(checks) board_tmp.copy(turn = board_tmp.nextTurn()).isCheckmate() else false
      println("checkmate: " + checkmate)
      if(valid) {
        undoManager.doStep(board, MoveCommand(move, checks))
      }
      else
        undoManager.noStep(board, MoveCommand(move))
    }
    else {
      undoManager.noStep(board, MoveCommand(move))
    }
  }

  def whichTurn(): PieceColor = board.turn

  def captureStacks() = {
    val (whiteCaptureStack, blackCaptureStack) = board.captureStacksStr()
    (whiteCaptureStack, blackCaptureStack)
  }

  def undo: Board = undoManager.undoStep(board)
  def redo: Board = undoManager.redoStep(board)
  def quit: Unit = notifyObservers(Event.Quit)

  def squareData(): List[(String, String, String)] = {
    board.squares.toSeq.sortBy(_._1.print_ord).map(square => (square._1.toString.toLowerCase, square._2.getOrElse("").toString, square._1.color.toString.toLowerCase)).toList
  }

  override def toString: String = board.toString

}
