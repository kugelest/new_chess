package htwg.se.chess
package controller

import model.boardComponent.Board
import model.boardComponent.Move
import util.Command
import util.UndoManager

class MoveCommand(move: Move) extends Command[Board] {
  override def noStep(board: Board): Board = board
  override def doStep(board: Board): Board = board.doMove(move.from, move.to)
  override def undoStep(board: Board): Board =
    board.undoMove(move.to, move.from)
  override def redoStep(board: Board): Board =
    board.doMove(move.from, move.to)
}
