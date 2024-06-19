package htwg.se.chess
package model
package boardComponent

import org.mongodb.scala._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import com.typesafe.config.ConfigFactory

import boardBaseImpl.Board
import boardComponent.BoardDAOPostgres
import boardComponent.BoardDAOMongodb

trait BoardDAO {
  def createTable(): Future[Unit]
  def save(boards: Seq[Board]): Future[Unit]
  def load(): Future[Set[Board]]
}

object BoardDAO {
  def apply(dbType: String)(implicit ec: ExecutionContext): BoardDAO = {
    val config = ConfigFactory.load()

    dbType match {
      case "postgres" =>
        val db = Database.forConfig("mydb")
        new BoardDAOPostgres(db)
      case "mongodb" =>
        val mongoUri = config.getString("mongodb.uri")
        val dbName = config.getString("mongodb.database")
        val mongoClient = MongoClient(mongoUri)
        new BoardDAOMongodb(mongoClient, dbName)
      case _ =>
        throw new IllegalArgumentException(s"Unsupported database type: $dbType")
    }
  }
}
