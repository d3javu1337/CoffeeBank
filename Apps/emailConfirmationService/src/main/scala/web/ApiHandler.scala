package web

import io.github.zeal18.zio.mongodb.driver.MongoDatabase
import mongo.{EmailConfirmDocumentDAL, MongoBase}
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serde
import zio.{RIO, RLayer, Scope, ZIO, ZLayer}

final case class ApiHandler() {

  def confirmEmail(token: String): RIO[Any, Any] = {
    (for {
      mongodb <- ZIO.service[EmailConfirmDocumentDAL]
      producer <- Producer.make(ProducerSettings(List("localhost:59092")))
      document <- mongodb.get(token)
      rec <- {
        producer.produce[Any, String, String](
          topic = "client_confirmation_email_response-topic",
          key = "",
          value = document.get.email,
          keySerializer = Serde.string,
          valueSerializer = Serde.string
        )
      }
    } yield rec.partition())
      .provideSome(EmailConfirmDocumentDAL.live, MongoBase.database, Scope.default)
  }
}

object ApiHandler {
  val layer: RLayer[Any, ApiHandler] = ZLayer.derive[ApiHandler]
}