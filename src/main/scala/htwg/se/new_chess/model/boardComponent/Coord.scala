package htwg.se.new_chess.model.boardComponent

private enum SquareColors {
  case BLACK, WHITE
}

enum Coord {
  case A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2, D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3, A4, B4, C4, D4,
    E4, F4, G4, H4, A5, B5, C5, D5, E5, F5, G5, H5, A6, B6, C6, D6, E6, F6, G6, H6, A7, B7, C7, D7, E7, F7, G7, H7, A8,
    B8, C8, D8, E8, F8, G8, H8

  def file = this.toString.charAt(0)
  def rank = this.toString.charAt(1)
  def color: SquareColors = if ((this.file + this.rank) % 2 == 0) SquareColors.WHITE else SquareColors.BLACK
  def print_ord: Int = Coord.len * (Coord.len - this.rank.asDigit) + (this.file - 'A')

  def neighbor(x: Int, y: Int) = Coord.fromTupel((this.file + x).toChar, (this.rank + y).toChar)
  def left_neighbor: Coord = this.neighbor(-1, 0)
  def right_neighbor: Coord = this.neighbor(1, 0)
  def upper_neighbor: Coord = this.neighbor(0, 1)
  def lower_neighbor: Coord = this.neighbor(0, -1)
  def upper_left_neighbor: Coord = this.neighbor(-1, 1)
  def lower_left_neighbor: Coord = this.neighbor(-1, -1)
  def upper_right_neighbor: Coord = this.neighbor(1, 1)
  def lower_right_neighbor: Coord = this.neighbor(1, -1)
}

object Coord {
  def len = 8
  def fromTupel(file: Char, rank: Char): Coord = Coord.valueOf(file.toString + rank.toString)
  def fromStr(str: String) = Coord.valueOf(str.toUpperCase())
}
