package org.d3javu
package domain

import domain.business.Invoice

object Errors {
  case object CardCreateError extends Exception("Error creating card")
  final case class InvoiceNotFound(id: Invoice.Id) extends Exception(s"Not found invoice with id ${id}")
}
