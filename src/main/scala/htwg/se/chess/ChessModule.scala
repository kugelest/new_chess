package htwg.se.chess

import model.boardComponent.BoardInterface
import model.boardComponent.boardBaseImpl
import model.boardComponent.MoveInterface
import model.fileIoComponent.FileIOInterface
import model.fileIoComponent.fileIoJsonImpl

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class ChessModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    // bind[BoardInterface].toInstance(Board())
    bind[BoardInterface].toInstance(boardBaseImpl.Board())
    bind[FileIOInterface].to[fileIoJsonImpl.FileIO]
    // bind[MoveInterface].to[Move]
    // bind[FileIOInterface].to[FileIOXMLImpl.FileIO]
  }

}
