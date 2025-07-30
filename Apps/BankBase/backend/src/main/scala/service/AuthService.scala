package service

import dao.mongo
import dao.mongo.SessionDocumentDAL
import dao.postgres.repository.ClientRepository
import errors.AuthError
import internalkafka.ProducerService
import internalkafka.messages.client.ClientCreateRequest
import security.JWT.TokenType.{ACCESS, REFRESH}
import security.JWT.{JWTService, Tokens}
import security.SecurityService
import service.AuthService.{LoginEnv, LogoutEnv, RefreshEnv, RegistrationEnv}
import web.requests.{ClientRegistration, Login}
import zio.{RIO, TaskLayer, ZIO, ZLayer}

case class AuthService() {

  def registration(req: ClientRegistration): RIO[RegistrationEnv, Boolean] = ZIO.scoped {
    for {
      _ <- ZIO.serviceWithZIO[ClientService](_.existsClientByEmail(req.email))
        .map(b => if b then ZIO.fail(Throwable("email already in use")))
      hash <- ZIO.serviceWithZIO[SecurityService](_.hashPassword(req.password))
      kafkaRequest <- ZIO.succeed(
        ClientCreateRequest(
          req.surname,
          req.name,
          req.patronymic,
          req.dateOfBirth,
          req.phoneNumber,
          req.email,
          hash))
      metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
      offset <- metadata.map(m => Option(m.offset()))
    } yield offset.isDefined
  }

  def login(loginRequest: Login, userAgent: String): RIO[LoginEnv, Tokens] =
    for {
      authData <- ZIO.serviceWithZIO[ClientService](_.loadAuthData(loginRequest.email))
      _ <- ZIO.serviceWithZIO[SecurityService](_.verifyPassword(loginRequest.password, authData.passwordHash))
        .flatMap(c => if !c then ZIO.fail(AuthError("credentials are wrong")) else ZIO.succeed(c))
      tokens <- ZIO.serviceWithZIO[JWTService](_.generateTokens(loginRequest.email))
      _ <- ZIO.serviceWithZIO[SessionService](_.registerSession(loginRequest.email, userAgent, tokens.accessToken, tokens.refreshToken))
    } yield tokens

  def refresh(refreshToken: String): RIO[RefreshEnv, Tokens] = for {
    email <- ZIO.serviceWithZIO[JWTService](_.getEmail(refreshToken, REFRESH))
    principal <- ZIO.serviceWithZIO[ClientService](_.loadPrincipal(email))
    tokens <- ZIO.serviceWithZIO[JWTService](_.generateTokens(principal.email))
    d <- ZIO.serviceWithZIO[SessionService](_.updateSessionOnTokensRefresh(principal.email, refreshToken, tokens))
  } yield tokens

  def logout(accessToken: String): RIO[LogoutEnv, Boolean] = for {
    email <- ZIO.serviceWithZIO[JWTService](_.getEmail(accessToken, ACCESS))
    res <- ZIO.serviceWithZIO[SessionService](_.logout(email, accessToken))
  } yield res

}

object AuthService {
  private type RegistrationEnv = ClientService &
    SecurityService &
    ProducerService &
    ClientRepository

  private type LoginEnv = JWTService &
    ClientService &
    SecurityService &
    SessionService &
    SessionDocumentDAL &
    ClientRepository

  private type RefreshEnv = JWTService &
    SessionService &
    SessionDocumentDAL &
    ClientService &
    ClientRepository

  private type LogoutEnv = SessionService &
    SessionDocumentDAL &
    JWTService

  val layer: TaskLayer[AuthService] = ZLayer.derive[AuthService]
}
