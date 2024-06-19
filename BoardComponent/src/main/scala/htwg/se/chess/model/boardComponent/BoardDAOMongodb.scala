package htwg.se.chess
package model
package boardComponent


import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.Promise
import scala.collection.JavaConverters.asScalaSetConverter
import scala.collection.JavaConverters.collectionAsScalaIterableConverter
import scala.collection.JavaConverters.iterableAsScalaIterableConverter

import boardBaseImpl.Board
import boardBaseImpl.Board
import boardBaseImpl.Coord
import boardBaseImpl.Move
import boardBaseImpl.pieces.Piece
import boardBaseImpl.pieces.PieceType
import boardBaseImpl.pieces.PieceColor


class BoardDAOMongodb(mongoClient: MongoClient, dbName: String)(implicit ec: ExecutionContext) extends BoardDAO {
  private val database: MongoDatabase = mongoClient.getDatabase(dbName)
  private val collection: MongoCollection[Document] = database.getCollection("boards")

  def createTable(): Future[Unit] = Future.successful(())

  override def save(boardsSeq: Seq[Board]): Future[Unit] = {
    val documents = boardsSeq.map { board =>
      Document(
        "id" -> board.id,
        "squares" -> Document(board.squares.map { case (coord, pieceOpt) =>
          coord.toString -> pieceOpt.map(piece =>
            Document(
              "type" -> piece.getClass.getSimpleName.toUpperCase,
              "color" -> piece.color.toString,
              "move_count" -> piece.move_count
            )
          ).getOrElse(Document("None" -> true))
        }),
        "turn" -> board.turn.toString,
        "in_check" -> board.in_check,
        "captured_pieces" -> board.captured_pieces.map(piece =>
          Document(
            "type" -> piece.getClass.getSimpleName.toUpperCase,
            "color" -> piece.color.toString,
            "move_count" -> piece.move_count
          )
        ),
        "moves" -> board.moves
      )
    }
    collection.insertMany(documents).toFuture().map(_ => ())
  }

  override def load(): Future[Set[Board]] = {
    collection.find().toFuture().map { documents =>
      documents.map { doc =>
        val id = doc.getInteger("id")
        val squares = doc.get("squares").map(_.asDocument().entrySet().asScala.map { entry =>
          val coordStr = entry.getKey
          val coord = Coord.valueOf(coordStr)
          val pieceOpt = entry.getValue.asDocument() match {
            case pieceDoc if pieceDoc.containsKey("None") => None
            case pieceDoc =>
              Some(Piece(
                PieceType.valueOf(pieceDoc.getString("type").getValue),
                PieceColor.valueOf(pieceDoc.getString("color").getValue),
                pieceDoc.getInteger("move_count")
              ))
          }
          coord -> pieceOpt
        }.toMap).getOrElse(Map.empty)
        val turn = PieceColor.valueOf(doc.getString("turn"))
        val inCheck = doc.getBoolean("in_check")
        val capturedPieces = doc.get("captured_pieces").map(_.asArray().getValues.asScala.map { value =>
          val pieceDoc = value.asDocument()
          Piece(
            PieceType.valueOf(pieceDoc.getString("type").getValue),
            PieceColor.valueOf(pieceDoc.getString("color").getValue),
            pieceDoc.getInteger("move_count")
          )
        }.toList).getOrElse(List.empty)
        val moves = doc.get("moves").map(_.asArray().getValues.asScala.map(_.asString().getValue).toList).getOrElse(List.empty)

        Board(id, squares, turn, inCheck, capturedPieces, moves)
      }.toSet
    }
  }

}
