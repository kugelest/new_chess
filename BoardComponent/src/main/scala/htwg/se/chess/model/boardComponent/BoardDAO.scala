package htwg.se.chess
package model
package boardComponent

import org.mongodb.scala._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import boardBaseImpl.Board

trait BoardDAO {
  def createTable(): Future[Unit]
  def save(boards: Seq[Board]): Future[Unit]
  def load(): Future[Set[Board]]
}

object BoardDAO {
  def apply(dbType: String)(implicit ec: ExecutionContext): BoardDAO = dbType match {
    case "postgres" =>
      val db = Database.forConfig("mydb")
      new BoardDAOPostgres(db)
    case "mongodb" =>
      val mongoClient = MongoClient("mongodb://localhost:27017")
      new BoardDAOMongodb(mongoClient, "testdb")
    case _ =>
      throw new IllegalArgumentException(s"Unsupported database type: $dbType")
  }
}
