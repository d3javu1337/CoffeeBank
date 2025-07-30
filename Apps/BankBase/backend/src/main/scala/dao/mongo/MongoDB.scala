package dao.mongo

import configuration.MongoConfig
import io.github.zeal18.zio.mongodb.driver
import io.github.zeal18.zio.mongodb.driver.{MongoClient, MongoDatabase}
import zio.{RLayer, Task, TaskLayer, ZIO, ZLayer}

final case class MongoDB(config: MongoConfig) {
  import config._

  private val client: TaskLayer[MongoClient] = MongoClient.live(s"mongodb://$username:$password@$host:$port/$authenticationDatabase")

  val db: TaskLayer[MongoDatabase] = client >>> ZLayer.fromZIO(ZIO.serviceWith[MongoClient](_.getDatabase(database)))
}

object MongoDB {
  val layer: RLayer[MongoConfig, MongoDatabase] = ZLayer.fromFunction(MongoDB.apply _).flatMap(_.get.db)
}
