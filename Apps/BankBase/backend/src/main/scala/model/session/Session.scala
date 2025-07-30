package model.session

import io.github.zeal18.zio.mongodb.bson.annotations.BsonId
import zio.json.{DeriveJsonCodec, JsonCodec}

import java.time.LocalDateTime
import java.util.UUID

case class Session(
                                   @BsonId _id: String,
                                   openedAt: LocalDateTime,
                                   lastInteractionTime: LocalDateTime,
                                   userAgent: String,
                                   accessToken: String,
                                   refreshToken: String
                                 ) {
  def this(userAgent: String, accessToken: String, refreshToken: String) = {
    this(UUID.randomUUID().toString, LocalDateTime.now(), LocalDateTime.now(), userAgent, accessToken, refreshToken)
  }

  inline given JsonCodec[Session] = DeriveJsonCodec.gen[Session]
}

object Session {
  def apply(userAgent: String, accessToken: String, refreshToken: String): Session =
    new Session(
      userAgent,
      accessToken,
      refreshToken
    )

  inline given JsonCodec[Session] = DeriveJsonCodec.gen
}

case class Sessions(
                     @BsonId _id: String,
                     sessions: List[Session]
                   )

object Sessions {
  inline given JsonCodec[Sessions] = DeriveJsonCodec.gen
}