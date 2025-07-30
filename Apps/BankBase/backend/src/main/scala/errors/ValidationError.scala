package errors

case class ValidationError(message: String) extends Throwable(message)
