package htwg.se.chess.model.boardComponent

import org.apache.pekko
import scala.collection.immutable

import pekko.actor.typed.ActorRef
import pekko.actor.typed.Behavior
import pekko.actor.typed.scaladsl.Behaviors
import scala.concurrent.ExecutionContext
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success
import scala.util.Failure

import boardBaseImpl.Board
import boardBaseImpl.Coord
import boardBaseImpl.Move

final case class Boards(boards: immutable.Seq[Board])

object BoardRegistry {
  sealed trait Command
  final case class GetBoard(id: Int, replyTo: ActorRef[GetBoardResponse]) extends Command
  final case class GetBoards(replyTo: ActorRef[Boards]) extends Command
  final case class GetBoardStr(id: Int, replyTo: ActorRef[GetBoardStrResponse]) extends Command
  final case class GetBoardsStr(replyTo: ActorRef[GetBoardsStrResponse]) extends Command
  final case class CreateBoard(replyTo: ActorRef[ActionPerformed]) extends Command
  final case class ExecMove(id: Int, move: Move, replyTo: ActorRef[ActionPerformed]) extends Command
  final case class Save(replyTo: ActorRef[ActionPerformed]) extends Command
  final case class Load(replyTo: ActorRef[ActionPerformed]) extends Command
  final case class BoardsLoaded(boards: Set[Board], replyTo: ActorRef[ActionPerformed]) extends Command
  final case class LoadFailed(ex: Throwable, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetBoardResponse(maybeBoard: Option[Board])
  final case class GetBoardStrResponse(maybeBoardStr: Option[String])
  final case class GetBoardsStrResponse(boardsStr: String)
  final case class ActionPerformed(description: String)

  // def apply(): Behavior[Command] = registry(Set.empty)

  def apply(): Behavior[Command] = Behaviors.setup { context =>
    // Initialize the database table
    implicit val ec: ExecutionContext = context.executionContext
    val db = BoardDAO("postgres")

    val createTableFuture = db.createTable()
    Await.result(createTableFuture, 10.seconds) // Block until the table is created (only for initialization)

    registry(Set.empty, db)(ec)
  }

  // val db = DAO()

  private def registry(boards: Set[Board], db: BoardDAO)(implicit ec: ExecutionContext): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetBoards(replyTo) =>
        replyTo ! Boards(boards.toSeq)
        Behaviors.same
      case GetBoardsStr(replyTo) =>
        replyTo ! GetBoardsStrResponse(boards.toSeq.map(_.toString).mkString("\n"))
        Behaviors.same
      case GetBoard(id, replyTo) =>
        replyTo ! GetBoardResponse(boards.find(_.id == id))
        Behaviors.same
      case GetBoardStr(id, replyTo) =>
        replyTo ! GetBoardStrResponse(boards.find(_.id == id).map(_.toString))
        Behaviors.same
      case CreateBoard(replyTo) =>
        replyTo ! ActionPerformed(s"New board created.")
        registry(boards + Board(), db)
      case ExecMove(id, move, replyTo) =>
        val boardOld = boards.find(_.id == id)
        val boardNew = boardOld.flatMap(_.move(move))
        (boardOld, boardNew) match {
          case (Some(o), Some(n)) =>
            replyTo ! ActionPerformed(s"Move executed.")
            registry(boards - o + n, db)
          case _ =>
            replyTo ! ActionPerformed(s"Move cannot be executed.")
            Behaviors.same
        }
      case Save(replyTo) =>
        val saveFuture = db.save(boards.toSeq)
        saveFuture.onComplete {
          case Success(_) =>
            println("Boards saved successfully")
            replyTo ! ActionPerformed("Boards saved successfully")
          case Failure(ex) =>
            println(s"Failed to save boards: ${ex.getMessage}")
            ex.printStackTrace()
            replyTo ! ActionPerformed(s"Failed to save boards: ${ex.getMessage}")
        }
        Behaviors.same
      case Load(replyTo) =>
        Behaviors.setup[Command] { context =>
          val loadFuture = db.load()
          loadFuture.onComplete {
            case Success(dbBoards) =>
              context.self ! BoardsLoaded(dbBoards, replyTo)
            case Failure(ex) =>
              context.self ! LoadFailed(ex, replyTo)
          }
          Behaviors.same
        }
      case BoardsLoaded(dbBoards, replyTo) =>
        println("Boards loaded successfully")
        replyTo ! ActionPerformed("Boards loaded successfully")
        registry(dbBoards, db)
      case LoadFailed(ex, replyTo) =>
        println(s"Failed to load boards: ${ex.getMessage}")
        ex.printStackTrace()
        replyTo ! ActionPerformed(s"Failed to load boards: ${ex.getMessage}")
        Behaviors.same
    }

}

