package mongo

import io.github.zeal18.zio.mongodb.bson.annotations.BsonId
import io.github.zeal18.zio.mongodb.driver.{MongoDatabase, ReadConcern, WriteConcern, filters}
import zio.{RLayer, Task, ZIO, ZLayer}

case class EmailConfirmDocument (@BsonId _id: String, email: String, hash: String)

trait EmailConfirmDocumentDAL {
  def insert(document: EmailConfirmDocument): Task[Boolean]
  def delete(hash: String): Task[Boolean]
  def get(hash: String): Task[Option[EmailConfirmDocument]]
  def count(): Task[Long]
}

object EmailConfirmDocumentDAL {
  val live: RLayer[MongoDatabase, EmailConfirmDocumentDAL] = ZLayer.scoped(
    for {
      mongo <- ZIO.service[MongoDatabase]
      coll = mongo
        .getCollection[EmailConfirmDocument]("EmailConfirmation")
        .withReadConcern(ReadConcern.MAJORITY)
        .withWriteConcern(WriteConcern.MAJORITY)
    } yield new EmailConfirmDocumentDAL:
      override def insert(document: EmailConfirmDocument): Task[Boolean] =
        coll.insertOne(document).map(_.wasAcknowledged())

      override def delete(hash: String): Task[Boolean] =
        coll.deleteOne(filters.eq[String]("hash", hash)).map(_.getDeletedCount > 0)

      override def get(hash: String): Task[Option[EmailConfirmDocument]] =
        coll.find(filters.eq("hash", hash)).runHead

      def count(): Task[Long] = {
        coll.countDocuments()
      }
  )
}
