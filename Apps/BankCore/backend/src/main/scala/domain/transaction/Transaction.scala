package org.d3javu
package domain.transaction

import enumeratum.EnumEntry
import enumeratum.Enum
import io.estatico.newtype.macros.newtype
import org.d3javu.domain.AccountID
import org.d3javu.domain.transaction.Transaction._

import java.time.LocalDateTime
import java.util.UUID

case class Transaction(
                      id: Id,
                      from: AccountID,
                      to: AccountID,
                      money: Money,
                      `type`: TransactionType,
                      isCompleted: IsCompleted,
                      commitedAt: CommitedAt
                      )

object Transaction {

  @newtype
  final case class Id(asUUID: UUID)

  @newtype
  final case class Money(asDouble: Double)

  @newtype
  final case class IsCompleted(asBoolean: Boolean)

  @newtype
  final case class CommitedAt(asLocalDateTime: LocalDateTime)

}

sealed trait TransactionType extends EnumEntry

object TransactionType extends Enum[TransactionType] {
  final case object PURCHASE extends TransactionType
  final case object TRANSFER extends TransactionType
  final case object WITHDRAW extends TransactionType
  final case object REPLENISH extends TransactionType

  override def values: IndexedSeq[TransactionType] = findValues
}