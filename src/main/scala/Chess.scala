

import controller.Controller
import model.BoardComponent.boardBaseImpl.Board
import aview.Tui
import aview.SwingGui

@main def run: Unit = {
  val board = Board()
  val controller = Controller(board)
  val swingGui = new SwingGui(controller)
  val tui = Tui(controller)
  tui.run
}
