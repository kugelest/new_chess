package htwg.se.chess

// import model.boardComponent.Board
// import model.boardComponent.Coord.*
// import model.boardComponent.Coord
// import model.boardComponent.MoveValidator
// import scala.io.StdIn
// import scala.util.Try
// import scala.util.Success
// import scala.annotation.tailrec
import controller.Controller
import model.boardComponent.Board
import aview.Tui
import aview.SwingGui

// @main def start: Unit =
//   println("Welcome!")
//   println("Type 'start' to start a new game.")
//   println("Type 'exit' to quit game.")
//   mainLoop(List(Board()))
//
@main def run: Unit = {
  val board = Board()
  val controller = Controller(board)
  // controller.add(FxGui)
  // FxGui.start()
  // val swingGui = new SwingGui(controller)
  val swingGui = new SwingGui(controller)
  val tui = Tui(controller)
  tui.run
}
//
// val movePattern = """move (\w{2}) (\w{2})""".r
//
// def processInput(input: String, board_list: List[Board]): List[Board] = {
//   val board = board_list.head
//   input match {
//     case "start" => {
//       val fresh_board_list = Board().startPos() :: List()
//       println("Starting new game...")
//       println("Type 'move e2 e4' e.g. to make a move.")
//       println("Type 'back' to take back last move.")
//       println()
//       println(fresh_board_list.head)
//       println()
//       fresh_board_list
//     }
//     case movePattern(from, to) => {
//       val coords_try: List[Try[Coord]] = List(Try(Coord.fromStr(from)), Try(Coord.fromStr(to)))
//       val coords = coords_try.collect { case Success(coord) => coord }
//       val coords_are_valid = coords.length == 2
//       val valid_move = coords_are_valid && MoveValidator.isMoveValid(coords(0), coords(1), board)
//       valid_move match {
//         case true => {
//           val new_board_list = board.makeMove(coords(0), coords(1)) :: board_list
//           println(new_board_list.head)
//           println()
//           new_board_list
//         }
//         case false => {
//           println("Invalid move! Try again...")
//           println()
//           println(board)
//           println()
//           board_list
//         }
//       }
//     }
//     case "back" => {
//       println("Take back last move...")
//       println()
//       println(board_list.tail.head)
//       println()
//       board_list.tail
//     }
//   }
//
// }
//
// @tailrec
// def mainLoop(board_list: List[Board]): Unit = {
//   val input = StdIn.readLine().toLowerCase()
//   println()
//
//   input match {
//     case "quit" | "exit" =>
//       println("Exiting...")
//     case _ =>
//       val new_board_list: List[Board] = processInput(input, board_list)
//       mainLoop(new_board_list)
//   }
// }
