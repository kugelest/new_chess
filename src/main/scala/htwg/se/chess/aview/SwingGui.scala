package htwg.se.chess
package aview

import controller.Controller
import model.BoardComponent.MoveInterface
import model.BoardComponent.boardBaseImpl.Move
import model.BoardComponent.boardBaseImpl.SquareColors
import model.BoardComponent.boardBaseImpl.pieces.PieceColor
import util.Event
import util.Observer

import javax.swing.BorderFactory
import java.awt.Color
import java.awt.Dimension
import scala.swing._
import scala.swing.event._
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._

class SwingGui(controller: Controller) extends Frame with Observer {
  val injector = Guice.createInjector(new ChessModule)

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
      contents += new MenuItem(Action("save")(controller.save))
      contents += new MenuItem(Action("load")(controller.doAndPublish(controller.load)))
    }
  }
  contents = updateContents
  pack()
  centerOnScreen()
  open()

  def updateContents = {
    val turn = controller.whichTurn()
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
    controller.board.squares
      .sortBy(_.coord.print_ord)
      .foreach(s => contents += SquareButton(s.coord.toString(), s.toString(), s.coord.color))
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
          controller.doAndPublish(controller.makeMove, Move(from, to))
        }
      }
    }
  }
}