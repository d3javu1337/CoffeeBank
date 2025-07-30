package model.personalaccount

import io.getquill.{InsertMeta, SchemaMeta, UpdateMeta, insertMeta, schemaMeta, updateMeta}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import zio.schema.{DeriveSchema, Schema}

case class PersonalAccount(
                            id: Long,
                            name: String = "",
                            deposit: Double,
                            clientId: Long,
                            accountType: AccountType
                          )

object PersonalAccount {
  inline given SchemaMeta[PersonalAccount] = schemaMeta[PersonalAccount](
    "personal_account",
    _.id -> "id",
    _.name -> "name",
    _.deposit -> "deposit",
    _.clientId -> "client_id",
    _.accountType -> "type"
  )

  inline given Schema[PersonalAccount] = DeriveSchema.gen[PersonalAccount]
}