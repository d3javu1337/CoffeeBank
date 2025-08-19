package service

import com.google.common.hash.Hashing
import com.mongodb.MongoWriteException
import configuration.{MailConfig, ServerConfig}
import emailing.Emailer
import jakarta.mail.Message.RecipientType
import jakarta.mail.Transport
import jakarta.mail.internet.MimeMessage
import mongo.{EmailConfirmDocument, EmailConfirmDocumentDAL}
import zio.{RIO, ULayer, ZIO, ZLayer}

import java.time.{LocalDateTime, ZoneOffset}


class MailService {

  def sendEmailConfirmation(email: String): RIO[EmailConfirmDocumentDAL & Emailer & ServerConfig & MailConfig, Unit] = {
    val hash = Hashing.sha256().hashBytes((email + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString).getBytes).toString
    for {
      mongo <- ZIO.service[EmailConfirmDocumentDAL]
      _ <- mongo.insert(EmailConfirmDocument(hash, email, hash))
        .catchSome {
          case writeException: MongoWriteException =>
            ZIO.logError(writeException.getMessage)
        }
      sender <- ZIO.serviceWith[MailConfig](_.sender)
      _ <- this.send(email = email, token = hash, sender)
    } yield ()
  }

  private def send(email: String, token: String, sender: String): RIO[Emailer & ServerConfig, Unit] = for {
    session <- ZIO.serviceWithZIO[Emailer](_.session)
    template <- Emailer.fillTemplate(token)
    _ <- ZIO.from(MimeMessage(session)).map(m => {
      m.setFrom(sender)
      m.setRecipients(RecipientType.TO, email)
      m.setSubject("Email confirmation")
      m.setContent(template, "text/html; charset=UTF-8")
      Transport.send(m)
    })
  } yield ()
}


object MailService {
  val layer: ULayer[MailService] = ZLayer.derive[MailService]
}