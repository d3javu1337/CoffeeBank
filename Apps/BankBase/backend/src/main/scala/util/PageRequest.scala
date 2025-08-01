package util

import zio.json.JsonCodec

case class PageRequest(
                        pageNumber: Int,
                        pageSize: Int
                      ) {
  require(pageNumber >= 0 && pageSize<=30, "page params are wrong")
}

object PageRequest {

}