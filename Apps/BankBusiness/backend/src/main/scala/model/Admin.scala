package model

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import zio.schema.{DeriveSchema, Schema}

import java.time.LocalDate

case class Admin (
    role: String,
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

object Admin {
  inline given SchemaMeta[Admin] = schemaMeta(
    "admin",
    _.id -> "id",
    _.surname -> "surname",
    _.name -> "name",
    _.patronymic -> "patronymic",
    _.dateOfBirth -> "date_of_birth",
    _.phoneNumber -> "phone_number",
    _.email -> "email",
    _.passwordHash -> "password_hash",
    _.isEnabled -> "is_enabled",
    _.role -> "role",
  )

  inline given InsertMeta[Admin] = insertMeta(_.id)

  inline given UpdateMeta[Admin] = updateMeta(_.id)

  implicit val businessClientSchema: Schema[Admin] = DeriveSchema.gen[Admin]

  implicit val encoder: JsonEncoder[Admin] = DeriveJsonEncoder.gen[Admin]

  implicit val decoder: JsonDecoder[Admin] = DeriveJsonDecoder.gen[Admin]
}