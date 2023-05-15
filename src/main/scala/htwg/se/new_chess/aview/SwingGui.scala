package htwg.se.chess
package aview

import controller.Controller
import model.boardComponent.Move
import model.boardComponent.pieces.PieceColor
import util.Event
import util.Observer

import scala.swing._
import scala.swing.event._

class SwingGui(controller: Controller) extends Frame with Observer {
  def squares = new SquarePanel()
  controller.add(this)
  title = "Chess"
  menuBar = new MenuBar {
    contents += new Menu("File") {
      contents += new MenuItem(Action("Exit") {
        sys.exit(0)
      })
    }
  }
  contents = updateContents
  pack()
  centerOnScreen()
  open()

  def updateContents = {
    new BorderPanel {
      add(new Label("Player: " + controller.PlayerState.player), BorderPanel.Position.North)
      add(squares, BorderPanel.Position.Center)
    }
  }

  def update(e: Event): Unit = e match
    case Event.Quit => this.dispose()
    case Event.Move => contents = updateContents; repaint()

  class SquarePanel() extends GridPanel(8, 8) {
    controller.board.squares.sortBy(_.coord.print_ord).foreach(s => contents += SquareButton(s.toString()))

  }

  class SquareButton(piece: String) extends Button(piece)

  // class CellPanel(x: Int, y: Int) extends GridPanel(8, 8) {
  //   (for (
  //     x <- 0 to 2;
  //     y <- 0 to 2
  //   ) yield (x, y, controller.get(x, y))).foreach(t => contents += new CellButton(t._1, t._2, t._3))
  // }
  //
  // def button(stone: String) = new Button(stone)
  //
  // class CellButton(x: Int, y: Int, stone: String) extends Button(stone) {
  //   listenTo(mouse.clicks)
  //   reactions += {
  //     case MouseClicked(src, pt, mod, clicks, props) => {
  //       controller.doAndPublish(controller.put, Move(controller.PlayerState.stone, x, y))
  //     }
  //   }
  // }
}
