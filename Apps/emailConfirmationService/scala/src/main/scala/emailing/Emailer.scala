package emailing

import cats.data.NonEmptyList
import cats.effect.IO
import emil.SSLType.{NoEncryption, SSL}
import emil.{Mail, MailConfig}
import emil.builder.{AttachUrl, From, MailBuilder, TextBody, To}
import emil.javamail.JavaMailEmil

object Emailer {

  private def fillTemplate(email: String, link: String): Mail[IO] = {
    MailBuilder.build(
      From("CoffeeBankMailer@gmail.com"),
      To(email),
      TextBody(
        s"""
          |<h1>Hi!</h1>
          |That is yours email confirmation link:
          |<a href=$link><b>CONFIRM</a>""".stripMargin
      )
    )
  }

  private def prepareLink(token: String) = {
    s"localhost:1337/confirm?token=$token"
  }

  private val mailer = JavaMailEmil[IO]()

  private val smtpConf = MailConfig(
    "smtp.gmail.com:25",
    "CoffeeBankMailer@gmail.com",
    "CoffeeBankMailer1337",
    NoEncryption
  )

  def sendConfirmationEmail(email: String, token: String): IO[NonEmptyList[String]] = {
    mailer(smtpConf)
      .send(this.fillTemplate(email = email, link = prepareLink(token)))
  }

}
