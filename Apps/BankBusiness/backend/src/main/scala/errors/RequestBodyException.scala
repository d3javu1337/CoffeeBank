package errors

case class RequestBodyException() extends Throwable("Wrong request body")
