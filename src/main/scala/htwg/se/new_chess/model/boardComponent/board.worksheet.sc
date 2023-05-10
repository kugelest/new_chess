import htwg.se.new_chess.model.boardComponent.Coord
import htwg.se.new_chess.model.boardComponent.Board

val s: Coord = Coord.A5
val b: Board = Board()

val print_ready = Coord.values.sortBy(x => x.print_ord)
print_ready.mkString(" ")
val p_poses = print_ready.map(_.print_ord)
p_poses.mkString(" ")

s.print_ord
s.toString
s.color
s.ordinal
s.right_neighbor

print(b.toString)

'A'.toInt

val seq = Seq(1, 2, 3)
seq.toString()
val set = Set(1, 2, 2)
