package htwg.se.chess
package model
package boardComponent

import slick.jdbc.PostgresProfile.api.*
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery

// import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

import boardStateTable._
import boardBaseImpl.Board

class BoardDAOPostgres()(implicit ec: ExecutionContext) extends BoardDAO {

  val db = Database.forConfig("mydb")

  val boards = TableQuery[BoardTable]

  override def createTable(): Future[Unit] = {
    db.run(boards.schema.create).map(_ => ())
  }

  override def save(boardsSeq: Seq[Board]): Future[Unit] = {
    val insertQuery = boards ++= boardsSeq
    db.run(insertQuery).map(_ => ())
  }

  override def load(): Future[Set[Board]] = {
    db.run(boards.result).map(_.toSet)
  }
}

