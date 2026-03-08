package org.d3javu

import io.estatico.newtype.macros.newtype

package object domain {

  // type only for transaction 'from' and 'to' field
  @newtype
  final case class AccountID(asLong: Long)

}
