package dao.repository

import dao.DatabaseContext
import dto.businessclient.BusinessClientReadDto
import model.BusinessClient
import zio.{RLayer, Task, URLayer, ZIO, ZLayer}

import java.sql.SQLException
import javax.sql.DataSource

trait BusinessClientRepository {
  def findById(id: Long): Task[Option[BusinessClient]]
  def findByEmail(email: String): Task[Option[BusinessClient]]
  def findIdByEmail(email: String): Task[Option[Long]]
  def findDtoByEmail(email: String): Task[Option[BusinessClientReadDto]]
}

case class BusinessClientRepositoryLive(private val ds: DataSource) extends BusinessClientRepository {

  import dao.DatabaseContext.*
  import io.getquill.*

  private val dsLayer = ZLayer.succeed(ds)

  private inline def businessClients = quote(query[BusinessClient])

  override def findById(id: Long): Task[Option[BusinessClient]] = {
    run(quote(businessClients.filter(_.id == lift(id))))
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findByEmail(email: String): Task[Option[BusinessClient]] = {
    run(quote(businessClients.filter(_.email == lift(email))))
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findIdByEmail(email: String): Task[Option[Long]] = {
    run(quote(businessClients.filter(_.email == lift(email)).map(_.id)))
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findDtoByEmail(email: String): Task[Option[BusinessClientReadDto]] = {
    run(quote{
      businessClients
        .filter(_.email == lift(email))
        .map(c => BusinessClientReadDto(c.officialName, c.brand))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }
}

object BusinessClientRepositoryLive {
  val layer: URLayer[DataSource, BusinessClientRepositoryLive] = ZLayer.fromFunction(BusinessClientRepositoryLive.apply _)
}