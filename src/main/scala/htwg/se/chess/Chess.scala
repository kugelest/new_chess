package htwg.se.chess

import controller.Controller
import model.BoardComponent.BoardInterface
import model.BoardComponent.BoardRestService
import model.FileIOComponent.FileIoRestService
import aview.Tui
import aview.SwingGui
import htwg.se.chess.aview.HttpServer

import com.google.inject.Guice

@main def run: Unit = {
  val injector = Guice.createInjector(new ChessModule)
  val board = injector.getInstance(classOf[BoardInterface])
  val controller = Controller(board)

  // val webserver = new HttpServer(controller)
  // val board_service = BoardRestService.main
  val file_io_service = FileIoRestService.main

  val swingGui = new SwingGui(controller)
  val tui = Tui(controller)
  tui.run
}
