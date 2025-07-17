import configuration.{JWTConfig, KafkaConfig}
import dao.repository.{BusinessClientRepository, BusinessClientRepositoryLive, ContactPersonRepositoryLive, PaymentAccountRepository, PaymentAccountRepositoryLive, PaymentRepositoryLive}
import dao.DatabaseContext
import io.grpc.ManagedChannelBuilder
import kafka.ProducerService
import org.d3javu.backend.grpc.invoice.ZioInvoice.InvoiceServiceClient
import scalapb.zio_grpc.ZManagedChannel
import security.JWT.JWTService
import security.{PasswordEncoder, Principal, SecurityService}
import service.{AuthService, BusinessClientService, ContactPersonService, InvoiceServiceImpl, PaymentAccountService, PaymentService}
import util.{Repository, Service}
import web.routes.*
import web.middleware.*
import zio.*
import zio.http.Server
import zio.http.netty.NettyConfig
import javax.sql.DataSource

object Main extends ZIOAppDefault {

  private val clientLayer = InvoiceServiceClient.live(
    ZManagedChannel(
      ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext()
    )
  )

  private val jwtConfig = JWTConfig.layer

  private val kafkaConfig = KafkaConfig.layer

  private val kafkaProducerServiceLayer = kafkaConfig >>> ProducerService.layer

  private val jwt = jwtConfig >>> JWTService.layer

  private val passwordEncoder = PasswordEncoder.live

  private val dataSource = DatabaseContext.live

  private val securityServiceLayer = passwordEncoder >>> SecurityService.layer

  private val repositoryLayer: URLayer[DataSource, Repository] =
    BusinessClientRepositoryLive.layer ++
      ContactPersonRepositoryLive.layer ++
      PaymentRepositoryLive.layer ++
      PaymentAccountRepositoryLive.layer

  private val businessClientServiceLayer: RLayer[BusinessClientRepository, BusinessClientService] = BusinessClientService.layer

  private val paymentAccountServiceLayer: RLayer[PaymentAccountRepository & BusinessClientRepository, PaymentAccountService] =
    businessClientServiceLayer >>> PaymentAccountService.layer

  private val serviceLayer: ZLayer[Repository, Throwable, Service] =
    businessClientServiceLayer ++
      (businessClientServiceLayer >>> ContactPersonService.layer) ++
      paymentAccountServiceLayer ++
      (paymentAccountServiceLayer >>> InvoiceServiceImpl.layer) ++
      (paymentAccountServiceLayer >>> PaymentService.layer)

  private val appLayers: ZLayer[Any & DataSource, Throwable, Service] =
    dataSource >>> jwtConfig >>> jwt >>> repositoryLayer >>> serviceLayer

  private val authServiceLayer: ZLayer[BusinessClientRepository & BusinessClientService & JWTService & SecurityService, Throwable, AuthService] =
    DatabaseContext.live >>> BusinessClientRepositoryLive.layer ++ BusinessClientService.layer ++ jwt ++ securityServiceLayer >>> AuthService.layer

  private val routes =
    AuthEndpoints.routes ++
      (BusinessClientEndpoints.routes ++
      ContactPersonEndpoints.routes ++
      InvoiceEndpoints.routes ++
      PaymentAccountEndpoints.routes ++
      PaymentEndpoints.routes) @@ JWTAuthMiddlewareWithContext

  private def serveRoutes(port: Int) = ZIO.scoped {
    Server.serve(routes)
      .provideSomeLayer(
        ZLayer.succeed(Server.Config.default.port(port)) ++
          ZLayer.succeed(NettyConfig.default.leakDetection(NettyConfig.LeakDetectionLevel.PARANOID)) >>>
          Server.customized
      )
  }

  override def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] = {
    (for {
      _ <- serveRoutes(1337)
    } yield ExitCode.success)
      .provide(
        dataSource,
        BusinessClientRepositoryLive.layer,
        appLayers,
        jwt,
        securityServiceLayer,
        authServiceLayer,
        InvoiceServiceImpl.layer,
        PaymentAccountRepositoryLive.layer,
        kafkaProducerServiceLayer,
        clientLayer
      )
  }

}