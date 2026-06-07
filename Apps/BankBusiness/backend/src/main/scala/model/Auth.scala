package model

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import zio.schema.{DeriveSchema, Schema}


case class Auth(
    id: Long,
    email: String,
    passwordHash: String,
    role: String
               )


object Auth {
  inline given SchemaMeta[Auth] = schemaMeta(
    "auth",
    _.id -> "id",
    _.email -> "email",
    _.passwordHash -> "password_hash",
    _.role -> "role"
  )

  inline given InsertMeta[Auth] = insertMeta(_.id)

  inline given UpdateMeta[Auth] = updateMeta(_.id)

  implicit val businessClientSchema: Schema[Auth] = DeriveSchema.gen[Auth]

  implicit val encoder: JsonEncoder[Auth] = DeriveJsonEncoder.gen[Auth]

  implicit val decoder: JsonDecoder[Auth] = DeriveJsonDecoder.gen[Auth]
}