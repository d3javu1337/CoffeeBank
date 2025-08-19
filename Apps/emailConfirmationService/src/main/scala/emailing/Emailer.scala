package emailing

import configuration.{MailConfig, ServerConfig}
import jakarta.mail.{Authenticator, PasswordAuthentication, Session}
import zio.{Task, URIO, URLayer, ZIO, ZLayer}

import java.util.Properties

class Emailer(private val config: MailConfig) {

  private val props = Properties()
  props.put("mail.smtp.host", config.host)
  props.put("mail.smtp.port", config.port)
  props.put("mail.smtp.auth", config.auth)
  props.put("mail.smtp.ssl.enable", config.sslEnable)

  val session: Task[Session] = ZIO.from(Session.getInstance(props, new Authenticator {
    override def getPasswordAuthentication: PasswordAuthentication =
      PasswordAuthentication(config.username, config.password)
  }))
}

object Emailer {

  def fillTemplate(token: String): URIO[ServerConfig, String] = {
    prepareLink(token).map(link =>
    s"""
       |<h1>Hi!</h1>
       |That is yours email confirmation link:
       |<a href=http://${link}><b>CONFIRM</a>""".stripMargin
    )
  }

  private def prepareLink(token: String): URIO[ServerConfig, String] = {
    ZIO.serviceWith[ServerConfig](_.port)
      .map(p => s"localhost:$p/confirm?token=$token")
  }

  val layer: URLayer[MailConfig, Emailer] = ZLayer.fromFunction(Emailer(_))

}
