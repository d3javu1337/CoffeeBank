package emailing

import jakarta.mail.Message.RecipientType
import jakarta.mail.internet.MimeMessage
import jakarta.mail.{Authenticator, PasswordAuthentication, Session, Transport}

import java.util.Properties

object Emailer {

  private def fillTemplate(token: String): String = {
    s"""
       |<h1>Hi!</h1>
       |That is yours email confirmation link:
       |<a href=http://${prepareLink(token)}><b>CONFIRM</a>""".stripMargin
  }

  private def prepareLink(token: String) = {
    s"localhost:1337/confirm?token=$token"
  }

  private val props = Properties()

  defineProps

  private def defineProps = {
    props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.port", "465")
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.ssl.enable", "true")
  }

  private val session = Session.getInstance(props, new Authenticator {
    override def getPasswordAuthentication: PasswordAuthentication =
      PasswordAuthentication("CoffeeBankMailer@gmail.com", "xose hnmq hrgw mcpu")
  })

  def sendConfirmationEmail(email: String, token: String): Unit = {
    val message = MimeMessage(session)
    message.setFrom("CoffeeBankMailer@gmail.com")
    message.setRecipients(RecipientType.TO, email)
    message.setSubject("test")
    message.setContent(this.fillTemplate(token), "text/html; charset=UTF-8")
    Transport.send(message)
  }
}
