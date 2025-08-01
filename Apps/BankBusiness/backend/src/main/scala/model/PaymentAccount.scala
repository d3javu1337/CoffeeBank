package model

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}

import java.util.UUID

case class PaymentAccount(
                           id: Long,
                           name: String,
                           deposit: Double,
                           businessClientId: Long,
                           invoiceCreateToken: UUID
                         )

object PaymentAccount {
  inline given SchemaMeta[PaymentAccount] = schemaMeta(
    "payment_account",
    _.id -> "id",
    _.name -> "name",
    _.deposit -> "deposit",
    _.businessClientId -> "business_client_id",
    _.invoiceCreateToken -> "invoice_create_token"
  )

  inline given InsertMeta[PaymentAccount] = insertMeta(_.id)

  inline given UpdateMeta[PaymentAccount] = updateMeta(_.id)
}