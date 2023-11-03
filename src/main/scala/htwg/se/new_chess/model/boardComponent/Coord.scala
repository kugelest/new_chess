package htwg.se.chess.model
package boardComponent

import scala.util.{Try, Success, Failure}
import scala.annotation.tailrec

enum Coord {
  case A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2, D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3, A4, B4, C4, D4,
    E4, F4, G4, H4, A5, B5, C5, D5, E5, F5, G5, H5, A6, B6, C6, D6, E6, F6, G6, H6, A7, B7, C7, D7, E7, F7, G7, H7, A8,
    B8, C8, D8, E8, F8, G8, H8

  def file = toString.charAt(0)
  def rank = toString.charAt(1)
  def color: SquareColors = if ((file + rank) % 2 == 0) SquareColors.WHITE else SquareColors.BLACK
  def print_ord: Int = Coord.len * (Coord.len - rank.asDigit) + (file - 'A')

  def neighbor(x: Int, y: Int): Try[Coord] = Try(Coord.fromTupel((file + x).toChar, (rank + y).toChar))
  def leftNeighbor(): Try[Coord] = neighbor(-1, 0)
  def rightNeighbor(): Try[Coord] = neighbor(1, 0)
  def upperNeighbor(): Try[Coord] = neighbor(0, 1)
  def lowerNeighbor(): Try[Coord] = neighbor(0, -1)
  def upperLeftNeighbor(): Try[Coord] = neighbor(-1, 1)
  def lowerLeftNeighbor(): Try[Coord] = neighbor(-1, -1)
  def upperRightNeighbor(): Try[Coord] = neighbor(1, 1)
  def lowerRightNeighbor(): Try[Coord] = neighbor(1, -1)

  def neighbors(f: Coord => Try[Coord])(): List[Coord] = {
    @tailrec
    def neighborsHelper(current: Coord, coord_acc: List[Coord]): List[Coord] = {
      f(current) match {
        case Success(n) => neighborsHelper(n, n :: coord_acc)
        case Failure(_) => coord_acc
      }
    }
    neighborsHelper(this, List()).reverse
  }
  def leftNeighbors = neighbors(_.leftNeighbor()) _
  def rightNeighbors = neighbors(_.rightNeighbor()) _
  def upperNeighbors = neighbors(_.upperNeighbor()) _
  def lowerNeighbors = neighbors(_.lowerNeighbor()) _
  def upperLeftNeighbors = neighbors(_.upperLeftNeighbor()) _
  def lowerLeftNeighbors = neighbors(_.lowerLeftNeighbor()) _
  def upperRightNeighbors = neighbors(_.upperRightNeighbor()) _
  def lowerRightNeighbors = neighbors(_.lowerRightNeighbor()) _
  // def diagonalNeighbors
  def upperTwo(): List[Coord] = List(neighbor(0, 1).get, neighbor(0, 2).get)
  def lowerTwo(): List[Coord] = List(neighbor(0, -1).get, neighbor(0, -2).get)
  def upperLeftAndRightNeighbor(): List[Coord] = List(upperLeftNeighbor(), upperRightNeighbor()).collect {
    case Success(value) => value
  }
  def lowerLeftAndRightNeighbor(): List[Coord] = List(lowerLeftNeighbor(), lowerRightNeighbor()).collect {
    case Success(value) => value
  }
  def upperFront(): List[Coord] = List(upperLeftNeighbor(), upperNeighbor(), upperRightNeighbor()).collect {
    case Success(value) => value
  }
  def lowerFront(): List[Coord] = List(lowerLeftNeighbor(), lowerNeighbor(), lowerRightNeighbor()).collect {
    case Success(value) => value
  }

  def knightNeighbors() = {
    List(
      neighbor(-2, 1),
      neighbor(-2, -1),
      neighbor(-1, 2),
      neighbor(-1, -2),
      neighbor(1, 2),
      neighbor(1, -2),
      neighbor(2, 1),
      neighbor(2, -1)
    ).collect { case Success(value) =>
      value
    }
  }
  def surroundingNeighbors() = {
    List(
      neighbor(0, 1),
      neighbor(0, -1),
      neighbor(1, 0),
      neighbor(1, 1),
      neighbor(1, -1),
      neighbor(-1, 0),
      neighbor(-1, 1),
      neighbor(-1, -1)
    ).collect { case Success(value) =>
      value
    }
  }
}

object Coord {
  def len = 8
  def fromTupel(file: Char, rank: Char): Coord = Coord.valueOf(file.toString + rank.toString)
  def fromStr(str: String) = Coord.valueOf(str.toUpperCase())
}

enum SquareColors {
  case BLACK, WHITE
}
