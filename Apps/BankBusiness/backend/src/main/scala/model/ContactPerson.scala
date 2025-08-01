package model

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class ContactPerson(
                          id: Long,
                          surname: String,
                          name: String,
                          patronymic: String,
                          phoneNumber: String,
                          email: String,
                          businessClientId: Long
                        )

object ContactPerson {
  inline given SchemaMeta[ContactPerson] = schemaMeta(
    "contact_person",
    _.id -> "id",
    _.surname -> "surname",
    _.name -> "name",
    _.patronymic -> "patronymic",
    _.phoneNumber -> "phone_number",
    _.email -> "email",
    _.businessClientId -> "business_client_id",
  )

  inline given InsertMeta[ContactPerson] = insertMeta(_.id)

  inline given UpdateMeta[ContactPerson] = updateMeta(_.id)

  implicit val encoder: JsonEncoder[ContactPerson] = DeriveJsonEncoder.gen[ContactPerson]
  implicit val decoder: JsonDecoder[ContactPerson] = DeriveJsonDecoder.gen[ContactPerson]
}