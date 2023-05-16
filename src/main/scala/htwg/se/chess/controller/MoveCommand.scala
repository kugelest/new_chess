package htwg.se.chess
package controller

import model.boardComponent.BoardInterface
import model.boardComponent.MoveInterface
import util.Command
import util.UndoManager

class MoveCommand(move: MoveInterface) extends Command[BoardInterface] {
  override def noStep(board: BoardInterface): BoardInterface = board
  override def doStep(board: BoardInterface): BoardInterface = board.doMove(move.from, move.to)
  override def undoStep(board: BoardInterface): BoardInterface =
    board.undoMove(move.to, move.from)
  override def redoStep(board: BoardInterface): BoardInterface =
    board.doMove(move.from, move.to)
}
