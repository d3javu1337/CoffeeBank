package mongo

import com.mongodb.MongoCredential
import io.github.zeal18.zio.mongodb.driver.{MongoClient, MongoClientSettings, MongoDatabase}
import zio.{ZIO, ZLayer}

object MongoBase {

  private val credential = MongoCredential.createScramSha256Credential("root", "admin", "root".toCharArray)

  private val settings: ZIO[Any, Nothing, MongoClientSettings] =
    ZIO.succeed(MongoClientSettings.builder()
      .credential(credential)
      .build())

  private val client: ZLayer[Any, Throwable, MongoClient] = MongoClient.live("mongodb://root:root@localhost:27017/admin")

  val database: ZLayer[Any, Throwable, MongoDatabase] =
    client >>> ZLayer.fromZIO(ZIO.serviceWith[MongoClient](_.getDatabase("email-confirm")))
}
