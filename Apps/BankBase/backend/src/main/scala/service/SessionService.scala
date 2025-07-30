package service

import dao.mongo.SessionDocumentDAL
import errors.NoEntityPresented
import io.github.zeal18.zio.mongodb.driver.result.{InsertOneResult, UpdateResult}
import model.session.Session
import security.JWT.TokenType.ACCESS
import security.JWT.{JWTService, Tokens}
import zio.{RIO, TaskLayer, ZIO, ZLayer}
import util.ZIOExtensions.*

case class SessionService() {
  def registerSession(clientEmail: String, userAgent: String, accessToken: String, refreshToken: String): RIO[SessionDocumentDAL, Boolean] = {
    ZIO.serviceWithZIO[SessionDocumentDAL](_.registerSession(clientEmail, Session(userAgent, accessToken, refreshToken)))
      .map({
        case r: InsertOneResult => r.wasAcknowledged()
        case r: UpdateResult => r.wasAcknowledged()
      })
      .customRetry(5, 100)
  }

  def logout(clientEmail: String, accessToken: String): RIO[SessionDocumentDAL, Boolean] = {
    ZIO.serviceWithZIO[SessionDocumentDAL](_.closeSessionByAccessToken(clientEmail, accessToken))
      .map(_.wasAcknowledged())
      .customRetry(5, 100)
  }

  def closeSessionsExclude(clientEmail: String, accessToken: String): RIO[SessionDocumentDAL, Boolean] = {
    ZIO.serviceWithZIO[SessionDocumentDAL](_.closeAllSessionsExclude(clientEmail, accessToken))
      .map(_.wasAcknowledged())
      .customRetry(5, 100)
  }

  def closeSession(clientEmail: String, sessionId: String): RIO[SessionDocumentDAL, Boolean] = {
    ZIO.serviceWithZIO[SessionDocumentDAL](_.closeSessionById(clientEmail, sessionId))
      .map(_.wasAcknowledged())
      .customRetry(5, 100)
  }

  def updateSessionOnTokensRefresh(clientEmail: String,
                                   oldRefreshToken: String, newTokens: Tokens): RIO[SessionDocumentDAL, Boolean] = {
    ZIO.serviceWithZIO[SessionDocumentDAL](_.updateSessionOnTokensRefresh(
        clientEmail,
        oldRefreshToken,
        newTokens.accessToken,
        newTokens.refreshToken
      ))
      .map(_.wasAcknowledged())
      .customRetry(5, 100)
  }

  def updateSessionOnInteraction(accessToken: String): RIO[SessionDocumentDAL & JWTService, Boolean] = {
    ZIO.serviceWithZIO[JWTService](_.getEmail(accessToken, ACCESS)).flatMap(
      email => ZIO.serviceWithZIO[SessionDocumentDAL](_.updateSessionOnInteraction(email, accessToken))
        .map(_.wasAcknowledged())
        .customRetry(5, 100)
    )
  }

  def getAllSessions(clientEmail: String): RIO[SessionDocumentDAL, List[Session]] = {
    ZIO.serviceWithZIO[SessionDocumentDAL](_.findAllSessions(clientEmail)
        .someOrFail(NoEntityPresented()))
      .map(_.sessions)
      .customRetry(5, 100)
  }

  def getSession(clientEmail: String, sessionId: String): RIO[SessionDocumentDAL, Session] = {
    ZIO.serviceWithZIO[SessionDocumentDAL](_.findSession(clientEmail, sessionId))
      .someOrFail(NoEntityPresented())
      .map(_.sessions.headOption)
      .someOrFail(NoEntityPresented())
      .customRetry(5, 100)
  }
}

object SessionService {
  val layer: TaskLayer[SessionService] = ZLayer.derive[SessionService]
}