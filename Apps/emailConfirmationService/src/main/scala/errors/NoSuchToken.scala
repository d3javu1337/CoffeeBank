package errors

case class NoSuchToken() extends Throwable("No such token presented in confirm queue")
