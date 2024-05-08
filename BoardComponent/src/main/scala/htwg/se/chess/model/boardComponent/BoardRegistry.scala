package htwg.se.chess.model.boardComponent

import org.apache.pekko
import scala.collection.immutable

import pekko.actor.typed.ActorRef
import pekko.actor.typed.Behavior
import pekko.actor.typed.scaladsl.Behaviors

import boardBaseImpl.Board

// final case class Board(name: String, age: Int, countryOfResidence: String)
final case class Boards(boards: immutable.Seq[Board])

object BoardRegistry {
  sealed trait Command
  final case class GetBoards(replyTo: ActorRef[Boards]) extends Command
  final case class GetBoard(name: String, replyTo: ActorRef[GetBoardResponse]) extends Command
  final case class CreateBoard(replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetBoardResponse(maybeBoard: Option[Board])
  final case class ActionPerformed(description: String)

  // val board = Board()

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(boards: Set[Board]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetBoards(replyTo) =>
        replyTo ! Boards(boards.toSeq)
        Behaviors.same
      case CreateBoard(replyTo) =>
        replyTo ! ActionPerformed(s"New board created.")
        registry(boards + Board())
    }
}

