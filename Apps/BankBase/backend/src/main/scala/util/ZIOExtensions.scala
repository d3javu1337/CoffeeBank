package util

import zio.{Duration, Schedule, ZIO}

object ZIOExtensions {
  extension[R, E, A] (eff: ZIO[R, E, A]) def customRetry(times: Int, spaceMillis: Int): ZIO[R, E, A] =
    eff.retry(Schedule.recurs(times) && Schedule.spaced(Duration.fromMillis(spaceMillis)))
}

