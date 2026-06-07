package dao.repository

import model.Auth
import zio.{Task, URLayer, ZLayer}

import javax.sql.DataSource

trait AuthRepository {
  def findByEmail(email: String): Task[Option[Auth]]

}

case class AuthRepositoryLive(private val ds: DataSource) extends AuthRepository {

  import dao.DatabaseContext.*
  import io.getquill.*

  private val dsLayer = ZLayer.succeed(ds)

  private inline def auth = quote(query[Auth])

  override def findByEmail(email: String): Task[Option[Auth]] = {
    run(quote{
      auth.filter(a => a.email == lift(email))
    }).mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provide(dsLayer)
  }
}

object AuthRepositoryLive {
  val layer: URLayer[DataSource, AuthRepositoryLive] = ZLayer.fromFunction(AuthRepositoryLive.apply _)

}