package org.d3javu
package service.base.card

trait CardService[F[_]] {
  def createCard: F[Unit]
  def renameCard: F[Unit]
}
