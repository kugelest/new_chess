package htwg.se.chess

import controller.Controller
import model.BoardComponent.BoardInterface
import aview.Tui
import aview.SwingGui

import com.google.inject.Guice

@main def run: Unit = {
  val injector = Guice.createInjector(new ChessModule)
  val board = injector.getInstance(classOf[BoardInterface])
  val controller = Controller(board)
  val swingGui = new SwingGui(controller)
  val tui = Tui(controller)
  tui.run
}
