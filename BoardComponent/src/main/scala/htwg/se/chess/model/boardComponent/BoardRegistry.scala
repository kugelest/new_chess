package htwg.se.chess.model.boardComponent

import org.apache.pekko
import scala.collection.immutable

import pekko.actor.typed.ActorRef
import pekko.actor.typed.Behavior
import pekko.actor.typed.scaladsl.Behaviors

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

  final case class GetBoardResponse(maybeBoard: Option[Board])
  final case class GetBoardStrResponse(maybeBoardStr: Option[String])
  final case class GetBoardsStrResponse(boardsStr: String)
  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  val db = DAO()

  private def registry(boards: Set[Board]): Behavior[Command] =
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
        registry(boards + Board())
      case ExecMove(id, move, replyTo) =>
        val board_old = boards.find(_.id == id)
        val board_new = board_old.flatMap(_.move(move))
        (board_old, board_new) match {
          case (Some(o), Some(n)) => {
            replyTo ! ActionPerformed(s"Move executed.")
            registry(boards - o + n)
          }
          case _ => {
            replyTo ! ActionPerformed(s"Move cannot be executed.")
            registry(boards)
          }
        }
      case Save(replyTo) =>
        db.save(boards.toSeq)
        replyTo ! ActionPerformed(s"boards saved")
        Behaviors.same
    }
}

