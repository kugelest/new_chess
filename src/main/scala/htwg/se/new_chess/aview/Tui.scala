package htwg.se.chess
package aview

import controller.Controller
import util.Observer
import util.Event

import scala.io.StdIn.readLine
import model.boardComponent.Move

class Tui(controller: Controller) extends Observer {

  controller.add(this)
  var continue = true

  def run = {
    println(controller.board.toString)
    inputLoop()
  }

  override def update(e: Event) = {
    e match {
      case Event.Quit => continue = false
      case Event.Move => println(controller.board.toString)
    }
  }

  val movePattern = """move (\w{2}) (\w{2})""".r

  def inputLoop(): Unit = {
    readLine() match {
      case "quit"                => controller.quit; None
      case "redo"                => controller.doAndPublish(controller.redo); None
      case "undo"                => controller.doAndPublish(controller.undo); None
      case movePattern(from, to) => controller.doAndPublish(controller.makeMove, Move(from, to))
      case _                     =>
    }
    if continue then inputLoop()
  }

}
