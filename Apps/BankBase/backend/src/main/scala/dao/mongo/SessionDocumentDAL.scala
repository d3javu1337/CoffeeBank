package dao.mongo

import com.mongodb.{ReadConcern, WriteConcern}
import io.github.zeal18.zio.mongodb.bson.codecs.Codec
import io.github.zeal18.zio.mongodb.driver
import io.github.zeal18.zio.mongodb.driver.result.{DeleteResult, InsertOneResult, UpdateResult}
import io.github.zeal18.zio.mongodb.driver.{MongoClient, MongoDatabase, filters, updates}
import model.session.{Session, Sessions}
import zio.{RLayer, Task, URLayer, ZIO, ZLayer}

import java.time.LocalDateTime


trait SessionDocumentDAL {
  def registerSession(clientEmail: String, session: Session): Task[UpdateResult | InsertOneResult]

  def closeSessionByAccessToken(clientEmail: String, accessToken: String): Task[DeleteResult]

  def closeSessionById(clientEmail: String, sessionId: String): Task[DeleteResult]

  def closeAllSessionsExclude(clientEmail: String, accessToken: String): Task[DeleteResult]

  def updateSessionOnTokensRefresh(clientEmail: String, oldRefreshToken: String,
                                   newAccessToken: String, newRefreshToken: String): Task[UpdateResult]

  def updateSessionOnInteraction(clientEmail: String, accessToken: String): Task[UpdateResult]

  def findAllSessions(clientEmail: String): Task[Option[Sessions]]

  def findSession(clientEmail: String, sessionId: String): Task[Option[Sessions]]
}

object SessionDocumentDAL {
  val layer: RLayer[MongoDatabase, SessionDocumentDAL] = ZLayer.scoped {
    for {
      mongo <- ZIO.service[MongoDatabase]
      coll = mongo
        .getCollection[Sessions]("Sessions")
        .withReadConcern(ReadConcern.MAJORITY)
        .withWriteConcern(WriteConcern.MAJORITY)
    } yield new SessionDocumentDAL:
      override def registerSession(clientEmail: String, session: Session): Task[UpdateResult | InsertOneResult] = for {
        sess <- findAllSessions(clientEmail)
        res <- if sess.isDefined
        then coll.updateOne(filters.eq("_id", clientEmail), updates.addToSet("sessions", session))
        else coll.insertOne(Sessions(clientEmail, List(session)))
      } yield res

      override def findAllSessions(clientEmail: String): Task[Option[Sessions]] =
        coll.find(filters.eq("_id", clientEmail)).runHead

      override def findSession(clientEmail: String, sessionId: String): Task[Option[Sessions]] =
        coll.find(filters.and(filters.equal("_id", clientEmail), filters.equal("sessions._id", sessionId))).runHead

      override def closeSessionById(clientEmail: String, accessToken: String): Task[DeleteResult] = {
        coll.deleteOne(
          filters.and(
            filters.equal("_id", clientEmail),
            filters.equal("sessions.accessToken", accessToken)
          ))
      }

      override def closeSessionByAccessToken(clientEmail: String, sessionId: String): Task[DeleteResult] = {
        coll.deleteOne(
          filters.and(
            filters.eq("_id", clientEmail),
            filters.eq("sessions._id", sessionId)
          )
        )
      }

      override def closeAllSessionsExclude(clientEmail: String, accessToken: String): Task[DeleteResult] = {
        coll.deleteMany(
          filters.and(
            filters.equal("_id", clientEmail),
            filters.notEqual("sessions.accessToken", accessToken),
            filters.lte("sessions.openedAt", LocalDateTime.now().minusMonths(1L))
          ))
      }

      override def updateSessionOnTokensRefresh(clientEmail: String, oldRefreshToken: String,
                                                newAccessToken: String, newRefreshToken: String): Task[UpdateResult] = {
        coll.updateOne(
          filters.and(
            filters.equal("_id", clientEmail),
            filters.equal("sessions.refreshToken", oldRefreshToken)
          ), Seq(
            updates.set(
              "sessions.accessToken", newAccessToken
            ),
            updates.set(
              "sessions.refreshToken", newRefreshToken
            ),
            updates.set(
              "sessions.lastInteractionTime", LocalDateTime.now()
            )
          )
        )
      }

      override def updateSessionOnInteraction(clientEmail: String, accessToken: String): Task[UpdateResult] = {
        coll.updateOne(
          filters.and(
            filters.equal("_id", clientEmail),
            filters.equal("sessions.accessToken", accessToken)
          ), Seq(
            updates.set(
              "sessions.lastInteractionTime", LocalDateTime.now()
            )
          )
        )
      }
  }
}
