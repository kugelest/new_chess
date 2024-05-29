package htwg.se.chess
package model
package boardComponent

import slick.jdbc.PostgresProfile.api.*
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

import boardStateTable._
import boardBaseImpl.Board

class DAO {

  private val databaseDB: String = sys.env.getOrElse("POSTGRES_DATABASE", "tbl")
  private val databaseUser: String = sys.env.getOrElse("POSTGRES_USER", "postgres")
  private val databasePassword: String = sys.env.getOrElse("POSTGRES_PASSWORD", "postgres")
  private val databasePort: String = sys.env.getOrElse("POSTGRES_PORT", "5432")
  private val databaseHost: String = sys.env.getOrElse("POSTGRES_HOST", "localhost")
  private val databaseUrl = s"jdbc:postgresql://$databaseHost:$databasePort/$databaseDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&autoReconnect=true"

  val db = Database.forURL(
      url = databaseUrl,
      driver = "org.postgresql.Driver",
      user = databaseUser,
      password = databasePassword
  )

  val boards = TableQuery[BoardTable]

  def createTable(): Future[Unit] = db.run(boards.schema.create)

  def save(boardsSeq: Seq[Board]): Future[Seq[Int]] = {
    val insertActions = boardsSeq.map { board =>
      (boards returning boards.map(_.id)) += board
    }
    db.run(DBIO.sequence(insertActions))
  }

}

