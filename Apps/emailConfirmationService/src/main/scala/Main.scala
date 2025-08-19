import configuration.{KafkaConfig, MailConfig, MongoConfig, ServerConfig}
import zio.*
import http.Server
import _root_.kafka.{KafkaConsumer, KafkaProducer}
import emailing.Emailer
import mongo.{EmailConfirmDocumentDAL, MongoDB}
import service.MailService
import web.{Handler, ConfirmRoutes}
import zio.http.netty.NettyConfig
import zio.http.netty.NettyConfig.LeakDetectionLevel.PARANOID

object Main extends ZIOAppDefault {

  private val configLayer = MongoConfig.layer ++ KafkaConfig.layer ++ MailConfig.layer ++ ServerConfig.layer

  private val mongoLayer = configLayer >>> MongoDB.layer >>> EmailConfirmDocumentDAL.live

  private val kafkaLayer = KafkaProducer.live ++ KafkaConsumer.live

  private val emailingLayer = MailService.layer ++ Emailer.layer

  private val serviceLayer = Handler.layer

  private def serveRoutes = ZIO.scoped {
    ZIO.serviceWith[ServerConfig](_.port).flatMap(p =>
    Server.serve(ConfirmRoutes.routes)
      .provideSomeLayer(
        ZLayer.succeed(Server.Config.default.port(p)) ++
          ZLayer.succeed(NettyConfig.default.leakDetection(PARANOID)) >>> Server.customized
      ))
  }

  private def startConsumer = ZIO.serviceWithZIO[KafkaConsumer](_.consume)

  override def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] = {
    (for {
      server <- serveRoutes.fork
      _ <- startConsumer
    } yield ())
      .provide(
        configLayer,
        mongoLayer,
        kafkaLayer,
        serviceLayer,
        emailingLayer
      )
  }
}