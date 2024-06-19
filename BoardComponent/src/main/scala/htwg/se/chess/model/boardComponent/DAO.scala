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

class DAO {

  val db = Database.forConfig("mydb")

  val boards = TableQuery[BoardTable]

  def createTable()(implicit ec: ExecutionContext): Future[Unit] = {
    println("Creating database table")
    db.run(boards.schema.create).map(_ => ())
  }

  def save(boardsSeq: Seq[Board])(implicit ec: ExecutionContext): Future[Unit] = {
    val insertQuery = boards ++= boardsSeq
    db.run(insertQuery).map(_ => ())
  }

  def load()(implicit ec: ExecutionContext): Future[Set[Board]] = {
    db.run(boards.result).map(_.toSet)
  }
}

