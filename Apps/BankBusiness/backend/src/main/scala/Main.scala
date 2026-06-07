import configuration.{JWTConfig, KafkaConfig}
import dao.repository.{AdminRepositoryLive, AuthRepositoryLive, BusinessClientRepository, BusinessClientRepositoryLive, ContactPersonRepositoryLive, InvoiceRepositoryLive, PaymentAccountRepository, PaymentAccountRepositoryLive, PaymentRepositoryLive}
import dao.DatabaseContext
import io.grpc.ManagedChannelBuilder
import kafka.ProducerService
import org.d3javu.backend.grpc.invoice.ZioInvoice.InvoiceServiceClient
import scalapb.zio_grpc.ZManagedChannel
import security.JWT.JWTService
import security.{PasswordEncoder, Principal, SecurityService}
import service.{AdminService, AuthService, BusinessClientService, ContactPersonService, InvoiceServiceImpl, PaymentAccountService, PaymentService}
import util.{Repository, Service}
import web.routes.*
import web.middleware.*
import zio.*
import zio.http.Middleware.cors
import zio.http.{Server, int}
import zio.http.netty.NettyConfig

import javax.sql.DataSource

object Main extends ZIOAppDefault {

  private val clientLayer = InvoiceServiceClient.live(
    ZManagedChannel(
      ManagedChannelBuilder.forAddress("core-backend", 9090).usePlaintext()
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
      PaymentAccountRepositoryLive.layer ++
      InvoiceRepositoryLive.layer ++
      AdminRepositoryLive.layer ++
      AuthRepositoryLive.layer

  private val businessClientServiceLayer: RLayer[BusinessClientRepository, BusinessClientService] = BusinessClientService.layer

  private val paymentAccountServiceLayer: RLayer[PaymentAccountRepository & BusinessClientRepository, PaymentAccountService] =
    businessClientServiceLayer >>> PaymentAccountService.layer

  private val serviceLayer: ZLayer[Repository, Throwable, Service] =
    businessClientServiceLayer ++
      (businessClientServiceLayer >>> ContactPersonService.layer) ++
      paymentAccountServiceLayer ++
      (paymentAccountServiceLayer >>> InvoiceServiceImpl.layer) ++
      (paymentAccountServiceLayer >>> PaymentService.layer) ++
      AdminService.layer

  private val appLayers: ZLayer[Any & DataSource, Throwable, Service] =
    dataSource >>> jwtConfig >>> jwt >>> repositoryLayer >>> serviceLayer

  private val authServiceLayer: ZLayer[JWTService & SecurityService, Throwable, AuthService] =
    DatabaseContext.live >>> AuthRepositoryLive.layer ++ jwt ++ securityServiceLayer >>> AuthService.layer

  private val routes = {
    AuthEndpoints.routes ++
      (BusinessClientEndpoints.routes ++
      ContactPersonEndpoints.routes ++
      InvoiceEndpoints.routes ++
      PaymentAccountEndpoints.routes ++
      PaymentEndpoints.routes) @@ JWTAuthMiddlewareWithContext ++
      (AdminEndpoints.routes @@ AdminMiddleware @@ JWTAuthMiddlewareWithContext) ++
      InvoiceEndpoints.pubRoute @@ cors(corsConfig)

  }
  private val emptyPrincipal: ULayer[Principal] = ZLayer.succeed(Principal(""))

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
        clientLayer,
        InvoiceRepositoryLive.layer,
        AdminRepositoryLive.layer,
      )
  }

}