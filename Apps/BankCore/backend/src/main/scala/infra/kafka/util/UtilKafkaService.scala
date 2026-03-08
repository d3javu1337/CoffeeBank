package org.d3javu
package infra.kafka.util

import infra.config.types.UtilKafkaConfig

class UtilKafkaService[F[_]](config: UtilKafkaConfig) {

  def produce = ???

  def onConsume = ???

}
