
package util

import model.BoardComponent.boardBaseImpl.Board

class UndoManager {
  private var undoStack: List[Board] = Nil
  private var redoStack: List[Board] = Nil

  def doStep(board: Board): Board = {
    undoStack = board :: undoStack
    redoStack = Nil
    board
  }

  def undoStep(board: Board): Board = {
    undoStack match {
      case cur :: rest => {
        undoStack = rest
        redoStack = cur :: redoStack
        rest.headOption.getOrElse(Board())
      }
      case Nil         => board
    }
  }

  def undoStepsTo(n: Int, board: Board): Board = {
    val (rest, undo) = undoStack.reverse.splitAt(n)
    undoStack = rest.reverse
    redoStack = undo ++ redoStack
    rest.lastOption match {
      case Some(b: Board) => b
      case _              => board
    }
  }

  def redoStep(board: Board): Board = {
    redoStack match {
      case cur :: rest => {
        redoStack = rest
        undoStack = cur :: undoStack
        cur
      }
      case Nil         => board
    }
  }

  def redoSteps(i: Int, board: Board): Board = {
    val (redo, rest) = redoStack.splitAt(i)
    redoStack = rest
    undoStack = redo.reverse ++ undoStack
    undoStack.head
  }

  def noStep(board: Board): Board = {
    board
  }

  def clear() = {
    undoStack = Nil
    redoStack = Nil
  }

  def redoStackMoves(): List[String] = {
    redoStack.map(_.moves.last)
  }
}
