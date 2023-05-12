package htwg.se.new_chess.model.boardComponent

trait Removable[A, B] {
  def remove(a: Seq[A], b: Seq[B]): Seq[A]
}
object Removable {
  given removeCoords: Removable[Square, Coord] with {
    def remove(a: Seq[Square], b: Seq[Coord]): Seq[Square] =
      a.filterNot(square => b.contains(square.coord))
  }
  given removeSquares: Removable[Square, Square] with {
    def remove(a: Seq[Square], b: Seq[Square]): Seq[Square] =
      a.diff(b)
  }
  extension [A](square_list: Seq[A]) {
    def remove[B](items: Seq[B])(using remover: Removable[A, B]): Seq[A] = {
      remover.remove(square_list, items)
    }
  }
}

trait Addable[A] {
  def add(a: Seq[A], b: Seq[A]): Seq[A]
}
object Addable {
  given addSquares: Addable[Square] with {
    def add(a: Seq[Square], b: Seq[Square]): Seq[Square] =
      a ++ b
  }
  extension [A](square_list: Seq[A]) {
    def add(items: Seq[A])(using adder: Addable[A]): Seq[A] = {
      adder.add(square_list, items)
    }
  }
}

trait Squareable[A, B] {
  def toSquare(a: A): B
}
object Squareable {
  given tupleToSquare: Squareable[Tuple2[Coord, Piece], Square] with {
    def toSquare(tuple: Tuple2[Coord, Piece]): Square =
      Square(tuple._1, Some(tuple._2))
  }
  given coordToSquare: Squareable[Coord, Square] with {
    def toSquare(coord: Coord): Square =
      Square(coord, None)
  }
  extension [A](tuple: A) {
    def toSquare[B]()(using squarer: Squareable[A, B]): B = {
      squarer.toSquare(tuple)
    }
  }
}

// extension (square_list: Seq[Square]) {
//   def replace(items: Seq[Square])(using remover: Removable[Square, Coord], adder: Addable[Square]): Seq[Square] = {
//     square_list.remove(items.map(_.coord)).add(items)
//   }
// }
