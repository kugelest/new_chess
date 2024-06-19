import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.Promise

class MongoBoardDAO(mongoClient: MongoClient, dbName: String)(implicit ec: ExecutionContext) extends BoardDAO {
  private val database: MongoDatabase = mongoClient.getDatabase(dbName)
  private val collection: MongoCollection[Document] = database.getCollection("boards")

  def createTable(): Future[Unit] = Future.successful(())

  def save(boardsSeq: Seq[Board]): Future[Unit] = {
    val documents = boardsSeq.map { board =>
      Document(
        "id" -> board.id,
        "squares" -> board.squares.map { case (coord, pieceOpt) =>
          s"${coord.x},${coord.y}" -> pieceOpt.map(piece => s"${piece.pieceType.toString},${piece.color.toString},${piece.move_count}").getOrElse("None")
        },
        "turn" -> board.turn.toString,
        "in_check" -> board.in_check,
        "captured_pieces" -> board.captured_pieces.map(piece => s"${piece.pieceType.toString},${piece.color.toString},${piece.move_count}"),
        "moves" -> board.moves
      )
    }
    collection.insertMany(documents).toFuture().map(_ => ())
  }

  def load(): Future[Set[Board]] = {
    collection.find().toFuture().map { documents =>
      documents.map { doc =>
        val id = doc.getInteger("id")
        val squares = doc.get("squares").map(_.asDocument().entrySet().map { entry =>
          val Array(x, y) = entry.getKey.split(",")
          val coord = Coord(x.toInt, y.toInt)
          val pieceOpt = entry.getValue.asString().getValue match {
            case "None" => None
            case pieceStr =>
              val Array(pieceType, color, moveCount) = pieceStr.split(",")
              Some(Piece(PieceType.valueOf(pieceType), PieceColor.valueOf(color), moveCount.toInt))
          }
          coord -> pieceOpt
        }.toMap).getOrElse(Map.empty)
        val turn = PieceColor.valueOf(doc.getString("turn"))
        val inCheck = doc.getBoolean("in_check")
        val capturedPieces = doc.get("captured_pieces").map(_.asArray().map { value =>
          val Array(pieceType, color, moveCount) = value.asString().getValue.split(",")
          Piece(PieceType.valueOf(pieceType), PieceColor.valueOf(color), moveCount.toInt)
        }.toList).getOrElse(List.empty)
        val moves = doc.get("moves").map(_.asArray().map(_.asString().getValue).toList).getOrElse(List.empty)

        Board(id, squares, turn, inCheck, capturedPieces, moves)
      }.toSet
    }
  }
}
