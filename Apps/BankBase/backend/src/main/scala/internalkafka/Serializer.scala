package internalkafka

import zio.ZIO
import zio.kafka.serde.Serde
import zio.json.*

extension (s: Serde.type) {
  def json[R, T: JsonCodec]: Serde[R, T] = {
    Serde.string.inmapZIO[Any, T](s =>
        ZIO.fromEither(s.fromJson[T])
          .mapError(e => Throwable(e)))
      (r => ZIO.succeed(r.toJson))
  }
}

object Serializer {}
