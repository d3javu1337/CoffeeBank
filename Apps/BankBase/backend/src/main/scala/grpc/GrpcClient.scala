package grpc

import configuration.GrpcConfig
import io.grpc.ManagedChannelBuilder
import org.d3javu.backend.grpc.transactions.ZioTransactions.TransactionServiceClient
import scalapb.zio_grpc.ZManagedChannel
import zio.{RLayer, ZIO, ZLayer}

case class GrpcClient(private val config: GrpcConfig) {
  private val grpcClient = TransactionServiceClient.live(
    ZManagedChannel(
      ManagedChannelBuilder.forAddress(config.host, config.port).usePlaintext()
    ) <* ZIO.logInfo("grpc client started")
  )
}

object GrpcClient {
  val layer: RLayer[GrpcConfig, TransactionServiceClient] = ZLayer.fromFunction(GrpcClient.apply _).flatMap(_.get.grpcClient)
}