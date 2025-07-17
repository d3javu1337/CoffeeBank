package security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import zio.{ULayer, ZLayer}

object PasswordEncoder {
  val live: ULayer[BCryptPasswordEncoder] = ZLayer.succeed(BCryptPasswordEncoder(13))
}
