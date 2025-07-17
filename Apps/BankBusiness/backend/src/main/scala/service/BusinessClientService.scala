package service

import dao.repository.{BusinessClientRepository, BusinessClientRepositoryLive}
import dto.businessclient.BusinessClientReadDto
import errors.NoEntityPresented
import model.BusinessClient
import zio.{RIO, RLayer, Task, URLayer, ZIO, ZLayer}

case class BusinessClientService(private val businessClientRepository: BusinessClientRepository) {

  def getById(id: Long): RIO[BusinessClientRepository, BusinessClient] = {
    businessClientRepository.findById(id)
      .someOrFail(NoEntityPresented())
  }

  def getByEmail(email: String): Task[BusinessClient] = {
    businessClientRepository.findByEmail(email)
      .someOrFail(NoEntityPresented())
  }

  def getIdByEmail(email: String): RIO[BusinessClientRepository, Long] = {
    businessClientRepository.findIdByEmail(email)
      .someOrFail(NoEntityPresented())
  }

  def getDtoByEmail(email: String): RIO[BusinessClientRepository, BusinessClientReadDto] = {
    businessClientRepository.findDtoByEmail(email)
      .someOrFail(NoEntityPresented())
  }

}

object BusinessClientService {
  val layer: RLayer[BusinessClientRepository, BusinessClientService] = ZLayer.fromFunction(BusinessClientService.apply _)
}
