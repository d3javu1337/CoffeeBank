package model.card

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import zio.schema.{DeriveSchema, Schema}

import java.util.Date

case class Card(
               id: Long,
               name: String,
               cardType: CardType,
               number: String,
               expirationDate: Date,
               accountId: Long,
               pinHash: Option[String],
               securityCode: String
               )

object Card {
  inline given SchemaMeta[Card] = schemaMeta(
    "card",
    _.id -> "id",
    _.name -> "name",
    _.cardType -> "type",
    _.number -> "number",
    _.expirationDate -> "expiration_date",
    _.accountId -> "account_id",
    _.pinHash -> "pin_hash",
    _.securityCode -> "security_code"
  )

  inline given Schema[Card] = DeriveSchema.gen[Card]
}