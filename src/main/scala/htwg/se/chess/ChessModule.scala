package htwg.se.chess

import model.BoardComponent.BoardInterface
import model.BoardComponent.boardBaseImpl
import model.BoardComponent.MoveInterface
import model.FileIOComponent.FileIOInterface
import model.FileIOComponent.fileIoJsonImpl

import com.google.inject.AbstractModule
import com.google.inject.Provider
import net.codingwell.scalaguice.ScalaModule

class BoardProvider extends Provider[BoardInterface] {
  override def get(): BoardInterface = {
    boardBaseImpl.Board()
  }
}

class ChessModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    // bind[BoardInterface].toInstance(Board())
    // bind[BoardInterface].toInstance(boardBaseImpl.Board())
    // bind[BoardInterface].to(boardBaseImpl.Board)
    bind[BoardInterface].toProvider[BoardProvider]
    bind[FileIOInterface].to[fileIoJsonImpl.FileIO]
    // bind[MoveInterface].to[Move]
    // bind[FileIOInterface].to[FileIOXMLImpl.FileIO]
  }

}
