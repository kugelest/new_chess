
package aview

import controller.Controller
import model.BoardComponent.boardBaseImpl.Move
import model.BoardComponent.boardBaseImpl.pieces.PieceColor
import model.BoardComponent.boardBaseImpl.SquareColor
import model.BoardComponent.boardBaseImpl.Coord
import util.Event
import util.Observer

import javax.swing.BorderFactory
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import scala.swing._
import scala.swing.event._

class SwingGui(controller: Controller) extends Frame with Observer {

  controller.add(this)
  def squares = new SquarePanel()

  title = "Chess"
  menuBar = new MenuBar {
    contents += new Menu("File") {
      contents += new MenuItem(Action("Exit")(controller.quit))
      // contents += new MenuItem(Action("Exit")(sys.exit(0)))
      contents += new MenuItem(Action("New Game")(controller.doAndPublish(controller.newGame)))
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
    val turn = controller.turn()
    new BorderPanel {
      add(new PlayerPanel(PieceColor.BLACK, turn), BorderPanel.Position.North)
      add(squares, BorderPanel.Position.Center)
      add(new PlayerPanel(PieceColor.WHITE, turn), BorderPanel.Position.South)
    }
  }

  def update(e: Event): Unit = e match
    // case Event.Quit => this.dispose()
    case Event.Quit => sys.exit(0)
    case Event.Move => contents = updateContents; repaint()

  class SquarePanel() extends GridPanel(8, 8) {
    controller.squareData()
      .foreach((coord, piece, square_color) => contents += SquareButton(coord, piece, square_color))
  }

  class PlayerPanel(color: PieceColor, turn: PieceColor) extends Label("Player: " + color.toString()) {
    opaque = true
    background = turn match {
      case t if t == color => Color.CYAN
      case _               => Color.WHITE
    }
  }

  var fromSet = false
  var from = ""
  var to = ""

  class SquareButton(coord: String, piece: String, square_color: SquareColor) extends Button(piece) {
    background = square_color match {
      case SquareColor.WHITE => Color.WHITE
      case SquareColor.BLACK => Color.LIGHT_GRAY
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
          background = square_color match {
            case SquareColor.WHITE => Color.WHITE
            case SquareColor.BLACK => Color.LIGHT_GRAY
          }
          controller.doAndPublish(controller.makeMove, Move(Coord.fromStr(from), Coord.fromStr(to)))
        }
      }
    }
  }
}
