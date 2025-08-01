package dao.postgres.repository

import dto.transaction.TransactionReadDto
import io.getquill.extras.===
import model.transaction.Transaction
import model.transaction.TransactionType.TRANSFER
import util.PageRequest
import zio.{Task, URLayer, ZIO, ZLayer}

import java.util.UUID
import javax.sql.DataSource

trait TransactionRepository {
  def findAllTransactionsPageable(accountId: Long, pageRequest: PageRequest): Task[List[TransactionReadDto]]

  def findTransactionById(accountId: Long, transactionId: UUID): Task[Option[TransactionReadDto]]
}

case class TransactionRepositoryLive(private val ds: DataSource) extends TransactionRepository {

  import io.getquill._
  import dao.postgres.DatabaseContext._
  import dao.postgres.DatabaseContext.given

  private val dsLayer = ZLayer.succeed(ds)

  private inline def transactions = quote(query[Transaction])

  override def findAllTransactionsPageable(accountId: Long, pageRequest: PageRequest): Task[List[TransactionReadDto]] = {
    run(quote {
      transactions
        .filter(t =>
          t.senderId == lift(accountId) || (t.transactionType == lift(TRANSFER) && t.recipientId === lift(accountId) && t.isCompleted))
        .drop(lift(pageRequest.pageNumber * pageRequest.pageSize))
        .take(lift(pageRequest.pageSize))
        .map(t => TransactionReadDto(
          t.id, t.senderId, t.recipientId, t.amount, t.transactionType, t.isCompleted, t.commitedAt
        ))
    })
      .mapError(e => Throwable(e.getMessage))
      .provideLayer(dsLayer)
  }

  override def findTransactionById(accountId: Long, transactionId: UUID): Task[Option[TransactionReadDto]] = {
    run(quote {
      query[Transaction]
        .filter(t =>
          t.id == lift(transactionId) && (t.senderId == lift(accountId) || t.recipientId.contains(lift(accountId))))
        .map(t => TransactionReadDto(t.id, t.senderId, t.recipientId, t.amount, t.transactionType, t.isCompleted, t.commitedAt))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }
}

object TransactionRepository {
  val layer: URLayer[DataSource, TransactionRepository] = ZLayer.fromFunction(TransactionRepositoryLive.apply _)
}