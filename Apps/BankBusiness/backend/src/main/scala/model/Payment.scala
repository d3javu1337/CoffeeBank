package model

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}

import java.util.UUID

case class Payment(
                  id: UUID,
                  paymentAccountId: Long,
                  invoiceId: UUID
                  )

object Payment {
  inline given SchemaMeta[Payment] = schemaMeta(
    "payment",
    _.id -> "id",
    _.paymentAccountId -> "payment_account_id",
    _.invoiceId -> "invoice_id"
  )

  inline given InsertMeta[Payment] = insertMeta(_.id)

  inline given UpdateMeta[Payment] = updateMeta(_.id)
}