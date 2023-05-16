package htwg.se.chess.model
package fileIoComponent

import boardComponent.BoardInterface

import scala.util.Try

trait FileIOInterface {

  def load: Try[Option[BoardInterface]]
  def save(grid: BoardInterface): Try[Unit]
  def unbind(): Unit

}
