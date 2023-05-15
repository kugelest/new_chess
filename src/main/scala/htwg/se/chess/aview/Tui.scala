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
    println("Welcome to Chess! \n")
    printInstructions()
    println(controller.board.toString + "\n")
    inputLoop()
  }

  override def update(e: Event) = {
    e match {
      case Event.Quit => continue = false
      case Event.Move => println(controller.board.toString + "\n")
    }
  }

  val movePattern = """move (\w{2}) (\w{2})""".r

  def inputLoop(): Unit = {
    readLine() match {
      case "start"               => controller.doAndPublish(controller.newGame)
      case "quit" | "exit"       => controller.quit
      case "redo"                => controller.doAndPublish(controller.redo)
      case "undo"                => controller.doAndPublish(controller.undo)
      case movePattern(from, to) => controller.doAndPublish(controller.makeMove, Move(from, to))
      case _                     =>
    }
    if continue then inputLoop()
  }

  def printInstructions() = {
    println("Instructions:")
    println("'start' to start a new Game")
    println("'quit' to exit Game")
    println("'move a2 a4' e.g. to make a move")
    println("'undo' to undo last move")
    println("'redo' to redo move")
    println()
  }

}
