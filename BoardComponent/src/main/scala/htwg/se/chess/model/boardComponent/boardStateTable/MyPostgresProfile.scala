package htwg.se.chess
package model
package boardComponent
package boardStateTable

import com.github.tminglei.slickpg._

trait MyPostgresProfile extends ExPostgresProfile
  with PgArraySupport
  with PgHStoreSupport {

    override protected def computeCapabilities: Set[slick.basic.Capability] = super.computeCapabilities + slick.jdbc.JdbcCapabilities.insertOrUpdate

    override val api = MyAPI

    object MyAPI extends ExtPostgresAPI
      with ArrayImplicits
      with HStoreImplicits {
      implicit val strListTypeMapper: BaseColumnType[List[String]] = new SimpleArrayJdbcType[String]("text").to(_.toList)
    }
}

object MyPostgresProfile extends MyPostgresProfile
