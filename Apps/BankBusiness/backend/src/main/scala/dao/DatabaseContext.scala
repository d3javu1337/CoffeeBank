package dao

import io.getquill.context.jdbc.Encoders
import io.getquill.jdbczio.Quill
import io.getquill.{PostgresZioJdbcContext, SnakeCase}
import zio.TaskLayer

import javax.sql.DataSource

object DatabaseContext extends PostgresZioJdbcContext(SnakeCase) with Encoders {
  val live: TaskLayer[DataSource] = Quill.DataSource.fromPrefix("db")
}
