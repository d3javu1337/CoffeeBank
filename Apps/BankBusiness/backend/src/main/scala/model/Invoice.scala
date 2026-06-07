package model

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}
import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

import java.util.UUID

case class Invoice(
                    id: UUID,
                    amount: Double,
                    providerPaymentAccountId: Long
                  )

object Invoice {
  inline given SchemaMeta[Invoice] = schemaMeta(
    "invoice",
    _.id -> "id",
    _.amount -> "amount",
    _.providerPaymentAccountId -> "provider_payment_account_id"
  )

  inline given InsertMeta[Invoice] = insertMeta(_.id)

  inline given UpdateMeta[Invoice] = updateMeta(_.id)

  implicit val encoder: JsonEncoder[Invoice] = DeriveJsonEncoder.gen[Invoice]
  implicit val decoder: JsonDecoder[Invoice] = DeriveJsonDecoder.gen[Invoice]

  given JsonCodec[Invoice] = DeriveJsonCodec.gen[Invoice]
}