package mongo

import io.github.zeal18.zio.mongodb.driver.{MongoClient, MongoDatabase}
import zio.{RLayer, ZIO, ZLayer}

object MongoBase {

//  private val client: ZLayer[Any, Throwable, MongoClient] = MongoClient.live("mongodb://localhost:27017")
  private val client: ZLayer[Any, Throwable, MongoClient] = MongoClient.live("mongodb://root:root@localhost:27017")

  val database: RLayer[Any, MongoDatabase] =
    client >>> ZLayer.fromZIO(ZIO.serviceWith[MongoClient](_.getDatabase("email-confirm")))
}
