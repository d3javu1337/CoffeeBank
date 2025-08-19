package web

import errors.NoSuchToken
import kafka.KafkaProducer
import mongo.EmailConfirmDocumentDAL
import zio.{RIO, RLayer, ZIO, ZLayer}

final case class Handler() {

  def confirmEmail(token: String): RIO[EmailConfirmDocumentDAL & KafkaProducer, Boolean] = ZIO.scoped {
    for {
      document <- ZIO.serviceWithZIO[EmailConfirmDocumentDAL](_.get(token))
      rec <- document match {
        case Some(doc) => ZIO.serviceWithZIO[KafkaProducer](_.produce(doc.email))
        case None => ZIO.fail(NoSuchToken())
      }
      _ <- ZIO.serviceWithZIO[EmailConfirmDocumentDAL](_.delete(token))
      partition <- rec.map(r => Option(r.partition()))
    } yield partition.isDefined
  }
}

object Handler {
  val layer: RLayer[Any, Handler] = ZLayer.derive[Handler]
}