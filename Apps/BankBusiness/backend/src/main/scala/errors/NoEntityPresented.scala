package errors

case class NoEntityPresented() extends Throwable("entity is not present")
