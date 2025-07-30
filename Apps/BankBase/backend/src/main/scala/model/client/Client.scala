package model.client

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import zio.schema.{DeriveSchema, Schema}

import java.time.LocalDate

case class Client(
                 id: Long,
                 surname: String,
                 name: String,
                 patronymic:String,
                 dateOfBirth: LocalDate,
                 phoneNumber: String,
                 email: String,
                 passwordHash: String,
                 isEnabled: Boolean
                 )

object Client {
  inline given SchemaMeta[Client] = schemaMeta(
    "client",
    _.id -> "id",
    _.surname -> "surname",
    _.name -> "name",
    _.patronymic -> "patronymic",
    _.dateOfBirth -> "date_of_birth",
    _.phoneNumber -> "phone_number",
    _.email -> "email",
    _.passwordHash -> "password_hash",
    _.isEnabled -> "is_enabled"
  )

  inline given Schema[Client] = DeriveSchema.gen[Client]
}