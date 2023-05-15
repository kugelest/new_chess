package htwg.se.chess
package aview

import controller.Controller
import model.boardComponent.Move
import model.boardComponent.pieces.PieceColor
import model.boardComponent.SquareColors
import htwg.se.chess.model.boardComponent.Square
import util.Event
import util.Observer

import javax.swing.BorderFactory
import java.awt.Color
import java.awt.Dimension
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
    contents += new Menu("Edit") {
      contents += new MenuItem(Action("Undo")(controller.doAndPublish(controller.undo)))
      contents += new MenuItem(Action("Redo")(controller.doAndPublish(controller.redo)))
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
    // background = Color.LIGHT_GRAY
    controller.board.squares
      .sortBy(_.coord.print_ord)
      .foreach(s => contents += SquareButton(s.coord.toString(), s.toString(), s.coord.color))

  }

  var fromSet = false
  var from = ""
  var to = ""

  class SquareButton(coord: String, piece: String, background_color: SquareColors) extends Button(piece) {
    background = background_color match {
      case SquareColors.WHITE => Color.WHITE
      case SquareColors.BLACK => Color.LIGHT_GRAY
    }
    border = BorderFactory.createEmptyBorder()
    preferredSize = new Dimension(100, 100)
    font = new Font("Monospace", 0, 75)
    focusPainted = false

    listenTo(mouse.clicks)
    reactions += {
      case MouseClicked(src, pt, mod, clicks, props) => {
        if (!fromSet) {
          fromSet = true
          from = coord
          background = Color.CYAN
        } else {
          fromSet = false
          to = coord
          background = background_color match {
            case SquareColors.WHITE => Color.WHITE
            case SquareColors.BLACK => Color.LIGHT_GRAY
          }
          // controller.move(from, to)
          controller.doAndPublish(controller.makeMove, Move(from, to))
        }
      }
    }
  }

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
