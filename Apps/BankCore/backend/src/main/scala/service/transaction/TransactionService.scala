package org.d3javu
package service.transaction

import backend.grpc.Transactions.TransactionServiceFs2Grpc

import io.grpc.Metadata

trait TransactionService[F[_]] extends TransactionServiceFs2Grpc[F, Metadata] {
}
