package dto.transaction

import model.transaction.TransactionType
import zio.json.{DeriveJsonCodec, JsonCodec}

import java.time.LocalDateTime
import java.util.UUID

case class TransactionReadDto(
                               id: UUID,
                               senderId: Long,
                               recipientId: Option[Long],
                               amount: Double,
                               transactionType: TransactionType,
                               isCompleted: Boolean,
                               commitedAt: LocalDateTime
                             )

object TransactionReadDto {
  inline given JsonCodec[TransactionReadDto] = DeriveJsonCodec.gen
}