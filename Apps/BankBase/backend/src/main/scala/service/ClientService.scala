package service

import dao.postgres.repository.ClientRepository
import dto.auth.AuthData
import dto.client.ClientReadDto
import errors.{AuthError, NoEntityPresented}
import security.Principal
import zio.{RIO, TaskLayer, URLayer, ZIO, ZLayer}

case class ClientService() {
  def getDtoById(clientId: Long): RIO[ClientRepository, ClientReadDto] = {
    ZIO.serviceWithZIO[ClientRepository](_.findDtoById(clientId))
      .someOrFail(NoEntityPresented())
  }

  def getDtoByEmail(clientEmail: String): RIO[ClientRepository, ClientReadDto] = {
    ZIO.serviceWithZIO[ClientRepository](_.findDtoByEmail(clientEmail))
      .someOrFail(NoEntityPresented())
  }

  def getIdByEmail(clientEmail: String): RIO[ClientRepository, Long] = {
    ZIO.serviceWithZIO[ClientRepository](_.findIdByEmail(clientEmail))
      .someOrFail(NoEntityPresented())
  }

  def existsClientByEmail(email: String): RIO[ClientRepository, Boolean] = {
    ZIO.serviceWithZIO[ClientRepository](_.existsClientByEmail(email))
  }

  def loadPrincipal(email: String): RIO[ClientRepository, Principal] = {
    ZIO.serviceWithZIO[ClientRepository](_.loadPrincipal(email))
      .someOrFail(AuthError("user is disabled"))
  }

  def loadAuthData(email: String): RIO[ClientRepository, AuthData] = {
    ZIO.serviceWithZIO[ClientRepository](_.loadAuthData(email))
      .someOrFail(AuthError(""))
  }

}

object ClientService {
  val layer: TaskLayer[ClientService] = ZLayer.derive[ClientService]
}