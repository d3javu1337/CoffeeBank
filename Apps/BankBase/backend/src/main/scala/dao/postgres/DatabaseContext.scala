package dao.postgres

import io.getquill.jdbczio.Quill
import io.getquill.{MappedEncoding, PostgresZioJdbcContext, SnakeCase}
import model.card.CardType
import model.personalaccount.AccountType
import zio.TaskLayer

import javax.sql.DataSource

object DatabaseContext extends PostgresZioJdbcContext(SnakeCase) {

  inline given MappedEncoding[CardType, String](_.toString)
  inline given MappedEncoding[String, CardType](CardType.valueOf)

  inline given MappedEncoding[AccountType, String](_.toString)
  inline given MappedEncoding[String, AccountType](AccountType.valueOf)

  val layer: TaskLayer[DataSource] = Quill.DataSource.fromPrefix("db")
}
