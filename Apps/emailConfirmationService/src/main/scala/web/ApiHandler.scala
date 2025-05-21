package web

import io.github.zeal18.zio.mongodb.driver.{MongoDatabase, ReadConcern, WriteConcern}
import kafka.KafkaProducer
import mongo.{EmailConfirmDocument, EmailConfirmDocumentDAL, MongoBase}
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serde
import zio.{IO, RIO, RLayer, Schedule, Scope, ZIO, ZLayer}

final case class ApiHandler() {

  def confirmEmail(token: String): RIO[Any, Any] = {
    (for {

      mongodb <- ZIO.service[MongoDatabase]
      coll = mongodb
        .getCollection[EmailConfirmDocument]("EmailConfirmation")
        .withReadConcern(ReadConcern.MAJORITY)
        .withWriteConcern(WriteConcern.MAJORITY)
      _ <- coll.find().runHead.map(d => zio.Console.printLine(d.isEmpty))
      mongo <- ZIO.service[EmailConfirmDocumentDAL]
      _ <- mongo.count().map(c => zio.Console.printLine(c))
        .catchSome(err => zio.Console.printLine(err.toString))
      _ <- zio.Console.printLine("123").fork
      producer <- Producer.make(ProducerSettings(List("localhost:59092"))).fork
      emailOpt <- mongo.get(token)
        .map(email =>
          zio.Console.printLine(s"email: ${email}")
//          producer.produce[Any, String, String](
//            topic = "client_confirmation_email_response-topic",
//            key = "",
//            value = email.get.email,
//            keySerializer = Serde.string,
//            valueSerializer = Serde.string
          ).fork
//          .schedule(Schedule.once)
//      )
//      email <- emailOpt.get
//        .catchSome(err => zio.Console.printLine(err))
//      _ <- KafkaProducer.produce("client_confirmation_email_response-topic", "", email.get.email)
//      _ <- Producer.produce("client_confirmation_email_response-topic", "", email.get.email, Serde.string, Serde.string)
//
//      _ <- zio.Console.printLine(email.get)
//      p <- producer.produce[Any, String, String](
//        topic = "client_confirmation_email_response-topic",
//        key = "",
//        value = email.get.email,
//        keySerializer = Serde.string,
//        valueSerializer = Serde.string
//      )
//        .schedule(Schedule.once)
      //        .fork
      _ <- mongo.delete(token).fork
    } yield ())
      .provideSome(EmailConfirmDocumentDAL.live, MongoBase.database, Scope.default)
  }
}

object ApiHandler {
  val layer: RLayer[Any, ApiHandler] = ZLayer.derive[ApiHandler]
}