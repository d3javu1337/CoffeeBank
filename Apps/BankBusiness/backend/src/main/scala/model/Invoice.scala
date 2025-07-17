package model

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}

import java.util.UUID

case class Invoice(
                    id: UUID,
                    amount: Double,
                    providerPaymentAccountId: Long
                  )

inline given SchemaMeta[Invoice] = schemaMeta(
  "invoice",
  _.id -> "id",
  _.amount -> "amount",
  _.providerPaymentAccountId -> "provider_payment_account_id"
)

inline given InsertMeta[Invoice] = insertMeta(_.id)

inline given UpdateMeta[Invoice] = updateMeta(_.id)