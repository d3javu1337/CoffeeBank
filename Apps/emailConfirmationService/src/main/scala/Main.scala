import com.google.common.hash.Hashing
import io.github.zeal18.zio.mongodb.driver.MongoDatabase
import kafka.{KafkaConsumer as Consumer, KafkaProducer as Producer}
import mongo.{EmailConfirmDocument, EmailConfirmDocumentDAL, MongoBase}
import zio.*
import http.Server
import web.{ApiHandler, Routing}

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] = {
    val email = "vanya@mail.ru"
    val document = EmailConfirmDocument(Hashing.sha256().hashBytes(email.getBytes).toString, 
      email, Hashing.sha256().hashBytes(email.getBytes).toString)
//    for {
//      consumer <- Consumer.consume(
//        List("localhost:59092"),
//        "mail-sender-consumer",
//        "client_email-confirmation-topic"
//      )
//    } yield()
    (for {
      server <- Server.serve(Routing.routes)
        .provide(Server.defaultWithPort(1337), ApiHandler.layer)
        .fork
      consumer <- Consumer.consume(
      List("localhost:59092"),
      "mail-sender-consumer",
      "client_email-confirmation-topic"
      ).orDie
    } yield () )

  }

//  override val bootstrap: ZLayer[MongoDatabase, Nothing, EmailConfirmDocumentDAL] = {
//    EmailConfirmDocumentDAL.live
//  }

}