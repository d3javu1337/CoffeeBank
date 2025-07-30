package web.responses.transaction

import zio.json.{DeriveJsonCodec, JsonCodec}

case class CompletionResponse(
                             isCompleted: Boolean
                             )

object CompletionResponse {
  inline given JsonCodec[CompletionResponse] = DeriveJsonCodec.gen
}