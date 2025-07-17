package service

import dao.repository.BusinessClientRepository
import kafka.ProducerService
import kafka.messages.businessclient.BusinessClientCreateRequest
import security.JWT.{JWTService, TokenType, Tokens}
import security.SecurityService
import util.KafkaMessage
import web.request.auth.{BusinessClientRegistration, Login}
import zio.{Cause, Duration, RIO, RLayer, Task, ZIO, ZLayer}

final case class AuthService(private val jwtService: JWTService,
                             private val businessClientService: BusinessClientService,
                             private val securityService: SecurityService
                            ) {

  def registration(registrationRequest: BusinessClientRegistration): RIO[ProducerService, Boolean] = ZIO.scoped {
    for {
      passwordHash <- securityService.hashPassword(registrationRequest.password)
      kafkaRequest <- ZIO.succeed(BusinessClientCreateRequest(
        registrationRequest.officialName,
        registrationRequest.brand,
        registrationRequest.email,
        passwordHash
      ))
      metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
        .timeoutFailCause(Cause.die(Throwable("timed out")))(Duration.fromSeconds(5))
      offset <- metadata.map(_.offset()).option
    } yield offset.isDefined
  }

  def login(loginRequest: Login): RIO[BusinessClientRepository, Tokens] = for {
    client <- businessClientService.getByEmail(loginRequest.email)
    isOk <- securityService.verifyPassword(loginRequest.password, client.passwordHash)
    accessToken <- jwtService.generateAccessToken(client.email)
    refreshToken <- jwtService.generateRefreshToken(client.email)
  } yield Tokens(accessToken, refreshToken)

  def refresh(refreshToken: String): Task[Tokens] = for {
    emailFromToken <- jwtService.getEmail(refreshToken, TokenType.REFRESH).map(_.get)
    accessToken <- jwtService.generateAccessToken(emailFromToken)
    refreshToken <- jwtService.generateRefreshToken(emailFromToken)
  } yield Tokens(accessToken, refreshToken)

}

object AuthService {
  val layer: RLayer[JWTService & BusinessClientService & SecurityService, AuthService] = ZLayer.fromFunction(AuthService.apply(_, _, _))
}