package dto.businessclient

import dto.contactperson.ContactPersonReadDto
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class BusinessClientCompactWithContacts (
                                   id: Long,
                                   officialName: String,
                                   brand: String,
                                   email: String,
                                   contacts: List[ContactPersonReadDto]
                                 )

object BusinessClientCompactWithContacts {
  implicit val encoder: JsonEncoder[BusinessClientCompactWithContacts] = DeriveJsonEncoder.gen[BusinessClientCompactWithContacts]
  implicit val decoder: JsonDecoder[BusinessClientCompactWithContacts] = DeriveJsonDecoder.gen[BusinessClientCompactWithContacts]
}
