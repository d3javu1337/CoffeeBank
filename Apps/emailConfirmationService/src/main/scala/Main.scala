import kafka.KafkaConsumer as Consumer
import zio.*
import http.Server
import web.{ApiHandler, Routing}

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] = {
    for {
      server <- Server.serve(Routing.routes)
        .provide(Server.defaultWithPort(1337), ApiHandler.layer)
        .fork
      consumer <- Consumer.consume(
        List("localhost:59092"),
        "mail-sender-consumer",
        "client_email-confirmation-topic"
      ).orDie
    } yield ()
  }
}