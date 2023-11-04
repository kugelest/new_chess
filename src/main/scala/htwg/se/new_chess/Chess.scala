package htwg.se.chess

import controller.Controller
import model.boardComponent.Board
import aview.Tui
import aview.SwingGui

@main def run: Unit = {
  val board = Board()
  val controller = Controller(board)
  val swingGui = new SwingGui(controller)
  val tui = Tui(controller)
  tui.run
}
