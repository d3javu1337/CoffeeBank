package kafka.messages.contactperson

import zio.json.{DeriveJsonCodec, JsonCodec}

case class ContactPersonDeleteRequest(
                                     businessClientEmail: String,
                                     personId: Long
                                     )

object ContactPersonDeleteRequest {
  implicit val codec: JsonCodec[ContactPersonDeleteRequest] = DeriveJsonCodec.gen[ContactPersonDeleteRequest]
}