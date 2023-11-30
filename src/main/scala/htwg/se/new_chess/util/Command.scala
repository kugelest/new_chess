package htwg.se.chess
package util

import model.boardComponent.Board

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
      case Nil                 => board
    }
  }

  def redoStep(board: Board): Board = {
    redoStack match {
      case cur :: rest         => {
        redoStack = rest
        undoStack = cur :: undoStack
        cur
      }
      case Nil                 => board
    }
  }

  def noStep(board: Board): Board = {
    board
  }

  def clear() = {
    undoStack = Nil
    redoStack = Nil
  }
}
