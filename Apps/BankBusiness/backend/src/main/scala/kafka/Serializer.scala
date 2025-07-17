package kafka

import zio.ZIO
import zio.json.*
import zio.kafka.serde.Serde

extension (s: Serde.type) {
  def json[R, T: JsonCodec]: Serde[R, T] =
    Serde.string.inmapZIO[Any, T](s =>
        ZIO.fromEither(s.fromJson[T])
          .mapError(e => Throwable(e)))
      (r => ZIO.succeed(r.toJson))
}


object Serializer {}
