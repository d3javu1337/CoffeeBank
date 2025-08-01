import configuration.{GrpcConfig, JWTConfig, KafkaConfig, MongoConfig}
import dao.mongo.{MongoDB, SessionDocumentDAL}
import dao.postgres.DatabaseContext
import dao.postgres.repository.{CardRepository, ClientRepository, InvoiceRepository, PaymentRepository, PersonalAccountRepository, TransactionRepository}
import grpc.GrpcClient
import internalkafka.ProducerService
import org.d3javu.backend.grpc.transactions.ZioTransactions.TransactionServiceClient
import security.JWT.JWTService
import security.{PasswordEncoder, SecurityService}
import service.{AuthService, CardService, ClientService, InvoiceService, PaymentService, PersonalAccountService, SessionService, TransactionService}
import web.middleware.JWTAuthMiddlewareWithContext
import web.routes.{AuthRoutes, CardRoutes, ClientRoutes, PersonalAccountRoutes, SessionsRoutes, TransactionRoutes}
import zio.*
import zio.http.netty.NettyConfig
import zio.http.netty.NettyConfig.LeakDetectionLevel.PARANOID
import zio.http.Server

object Main extends ZIOAppDefault {

  private val mongoLayer = MongoDB.layer >>> SessionDocumentDAL.layer

  private val routes = AuthRoutes.routes ++
    (
      ClientRoutes.routes ++
        CardRoutes.routes ++
        PersonalAccountRoutes.routes ++
        TransactionRoutes.routes ++
        SessionsRoutes.routes
      ) @@ JWTAuthMiddlewareWithContext

  private val repositoryLayer = ClientRepository.layer ++
    CardRepository.layer ++
    InvoiceRepository.layer ++
    PaymentRepository.layer ++
    PersonalAccountRepository.layer ++
    TransactionRepository.layer

  private val serviceLayer = AuthService.layer ++
    ProducerService.layer ++
    SecurityService.layer ++
    JWTService.layer ++
    SessionService.layer ++
    ClientService.layer ++
    PersonalAccountService.layer ++
    CardService.layer ++
    PaymentService.layer ++
    TransactionService.layer ++
    InvoiceService.layer

  private val configLayer = GrpcConfig.layer ++
    JWTConfig.layer ++
    KafkaConfig.layer ++
    MongoConfig.layer

  private val utilLayer = PasswordEncoder.live

  private def serveRoutes(port: Int) = ZIO.scoped {
    Server.serve(routes)
      .provideSomeLayer(
        ZLayer.succeed(Server.Config.default.port(port)) ++
          ZLayer.succeed(NettyConfig.default.leakDetection(PARANOID)) >>> Server.customized
      )
  }

  override def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] = {
    serveRoutes(8888)
      .provide(
        DatabaseContext.layer,
        repositoryLayer,
        serviceLayer,
        mongoLayer,
        configLayer,
        utilLayer,
        GrpcClient.layer
      )
  }
}
