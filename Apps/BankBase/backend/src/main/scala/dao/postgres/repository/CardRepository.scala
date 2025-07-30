package dao.postgres.repository

import dao.postgres.DatabaseContext
import dto.card.CardReadDto
import model.card.Card
import zio.{Task, URLayer, ZLayer}

import javax.sql.DataSource

trait CardRepository {
  def findListDtoByAccountId(accountId: Long): Task[List[CardReadDto]]
  def findDtoById(cardId: Long): Task[Option[CardReadDto]]
  def isCardBelongsToAccount(cardId: Long, accountId: Long): Task[Boolean]
}

final case class CardRepositoryLive(private val ds: DataSource) extends CardRepository{

  import io.getquill._
  import dao.postgres.DatabaseContext._
  import dao.postgres.DatabaseContext.given

  private val dsLayer = ZLayer.succeed(ds)

  private inline def cards = quote(query[Card])

  override def findListDtoByAccountId(accountId: Long): Task[List[CardReadDto]] = {
    run(quote{
      cards
        .filter(_.accountId == lift(accountId))
        .map(c => CardReadDto(c.id, c.name, c.cardType, c.number))
    })
      .mapError(e => Throwable(e.getMessage))
      .provideLayer(dsLayer)
  }

  override def findDtoById(cardId: Long): Task[Option[CardReadDto]] = {
    run(quote{
      cards
        .filter(_.id == lift(cardId))
        .map(c => CardReadDto(c.id, c.name, c.cardType, c.number))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def isCardBelongsToAccount(cardId: Long, accountId: Long): Task[Boolean] = {
    run(quote{
      cards
        .filter(c => c.id == lift(cardId) && c.accountId == lift(accountId))
        .size
    })
      .mapBoth(e => Throwable(e.getMessage), _==1)
      .provideLayer(dsLayer)
  }
}

object CardRepository {
  val layer: URLayer[DataSource, CardRepository] = ZLayer.fromFunction(CardRepositoryLive.apply _)
}