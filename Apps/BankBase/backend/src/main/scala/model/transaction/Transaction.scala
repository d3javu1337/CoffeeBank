package model.transaction

import io.getquill.{SchemaMeta, schemaMeta}
import zio.schema.{DeriveSchema, Schema}

import java.time.LocalDateTime
import java.util.UUID

case class Transaction(
                      id: UUID,
                      senderId: Long,
                      recipientId: Option[Long],
                      amount: Double,
                      transactionType: TransactionType,
                      isCompleted: Boolean,
                      commitedAt: LocalDateTime
                      )

object Transaction {
  inline given SchemaMeta[Transaction] = schemaMeta(
    "transaction",
    _.id -> "id",
    _.senderId -> "sender_id",
    _.recipientId -> "recipient_id",
    _.amount -> "amount",
    _.transactionType -> "type",
    _.isCompleted -> "is_completed",
    _.commitedAt -> "commited_at"
  )

  inline given Schema[Transaction] = DeriveSchema.gen[Transaction]
}