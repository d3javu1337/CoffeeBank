package errors

case class AuthError(message: String) extends Throwable(message)
