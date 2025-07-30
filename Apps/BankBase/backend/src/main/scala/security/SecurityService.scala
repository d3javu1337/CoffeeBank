package security

import errors.AuthError
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import zio.{RLayer, Task, ZIO, ZLayer}

case class SecurityService(private val passwordEncoder: BCryptPasswordEncoder) {

  def hashPassword(password: String): Task[String] = {
    ZIO.succeed(passwordEncoder.encode(password))
  }

  def verifyPassword(password: String, hashedPassword: String): Task[Boolean] = {
    ZIO.succeed(passwordEncoder.matches(password, hashedPassword))
      .flatMap({
        case true => ZIO.succeed(true)
        case false => ZIO.fail(AuthError("Wrong password"))
      })
  }
}

object SecurityService {
  val layer: RLayer[BCryptPasswordEncoder, SecurityService] = ZLayer.fromFunction(SecurityService.apply _)
}