package kafka.messages.contactperson

import zio.json.{DeriveJsonCodec, JsonCodec}

case class ContactPersonUpdateRequest(
                                       businessClientEmail: String,
                                       contactPersonId: Long,
                                       surname: String,
                                       name: String,
                                       patronymic: String,
                                       phoneNumber: String,
                                       email: String
                                     )

object ContactPersonUpdateRequest {
  implicit val codec: JsonCodec[ContactPersonUpdateRequest] = DeriveJsonCodec.gen[ContactPersonUpdateRequest]
}