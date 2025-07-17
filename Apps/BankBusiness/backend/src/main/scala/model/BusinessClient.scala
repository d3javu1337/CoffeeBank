package model

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import zio.schema.{DeriveSchema, Schema}

case class BusinessClient(
                           id: Long,
                           officialName: String,
                           brand: String,
                           email: String,
                           passwordHash: String
                         ) {

}

inline given SchemaMeta[BusinessClient] = schemaMeta(
  "business_client",
  _.id -> "id",
  _.officialName -> "official_name",
  _.brand -> "brand",
  _.email -> "email",
  _.passwordHash -> "password_hash"
)

inline given InsertMeta[BusinessClient] = insertMeta(_.id)

inline given UpdateMeta[BusinessClient] = updateMeta(_.id)

implicit val businessClientSchema: Schema[BusinessClient] = DeriveSchema.gen[BusinessClient]

object BusinessClient {
  implicit val encoder: JsonEncoder[BusinessClient] = DeriveJsonEncoder.gen[BusinessClient]
  implicit val decoder: JsonDecoder[BusinessClient] = DeriveJsonDecoder.gen[BusinessClient]
}