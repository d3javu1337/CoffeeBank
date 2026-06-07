package service

import dao.repository.{AdminRepository, AdminRepositoryLive}
import dto.businessclient.BusinessClientCompact
import dto.contactperson.ContactPersonReadDto
import errors.NoEntityPresented
import service.AdminService.ErrNoAdmin
import zio.{RIO, RLayer, Task, ZIO, ZLayer}

final case class AdminService (
                                private val adminRepository: AdminRepository
                              ) {
 def isAdmin(email: String): Task[Boolean] = {
   adminRepository.isAdmin(email)
 }

 def getAllClients: Task[List[BusinessClientCompact]] = {
   adminRepository.findAllClients
 }

 def getClientById(clientId: Long): Task[BusinessClientCompact] = {
   adminRepository
     .findClientById(clientId)
     .someOrFail(NoEntityPresented())
 }
 
 def getContactsByClientId(clientId: Long): Task[List[ContactPersonReadDto]] = {
   adminRepository
     .findAllContactsByClientId(clientId)
 }

}

object AdminService {

  sealed trait AdminServiceErrors extends Throwable

  final case class ErrNoAdmin() extends AdminServiceErrors

  val layer: RLayer[AdminRepository, AdminService] = ZLayer.fromFunction(AdminService.apply(_))
}