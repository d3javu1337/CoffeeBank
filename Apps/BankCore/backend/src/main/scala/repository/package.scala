package org.d3javu

package object repository {
  final case class DBError(err: Throwable) extends Exception(err)
}
