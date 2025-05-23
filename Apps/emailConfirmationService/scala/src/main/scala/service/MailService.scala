package service

import com.google.common.hash.Hashing
import com.mongodb.MongoWriteException
import emailing.Emailer
import mongo.{EmailConfirmDocument, EmailConfirmDocumentDAL, MongoBase}
import zio.ZIO


object MailService {

  def sendEmailConfirmation(email: String): ZIO[Any, Throwable, Unit] = {
    val hash = Hashing.sha256().hashBytes(email.getBytes).toString
    (for {
      mongo <- ZIO.service[EmailConfirmDocumentDAL]
      _ <-
        mongo.insert(EmailConfirmDocument(hash, email, hash)).catchSome {
          case writeException: MongoWriteException =>
            zio.Console.printLine("retry of confirmation request on account which already waiting")
          case _ => zio.Console.printLine("123")
        }
//          .as(Emailer.sendConfirmationEmail(email = email, token = hash))
    } yield ())
      .provideSome(EmailConfirmDocumentDAL.live, MongoBase.database)
  }

}
