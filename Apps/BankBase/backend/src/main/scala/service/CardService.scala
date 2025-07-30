package service

import dao.postgres.repository.{CardRepository, ClientRepository, PersonalAccountRepository}
import dto.card.CardReadDto
import errors.NoEntityPresented
import internalkafka.ProducerService
import internalkafka.messages.card.{CardCreateRequest, CardRenameRequest}
import model.card.CardType
import service.CardService.{GetCardsByClientEmailEnv, OpenCardEnv, RenameCardEnv}
import web.requests.card.{CardCreate, CardRename}
import zio.{RIO, Task, TaskLayer, URLayer, ZIO, ZLayer}

case class CardService() {
  def getCardsByClientEmail(clientEmail: String): RIO[GetCardsByClientEmailEnv, List[CardReadDto]] = for {
    accountId <- ZIO.serviceWithZIO[PersonalAccountService](_.getAccountIdByClientEmail(clientEmail))
    res <- ZIO.serviceWithZIO[CardRepository](_.findListDtoByAccountId(accountId))
  } yield res

  def getCardById(cardId: Long): RIO[CardRepository, CardReadDto] = {
    ZIO.serviceWithZIO[CardRepository](_.findDtoById(cardId))
      .someOrFail(NoEntityPresented())
  }

  def isCardBelongsToAccount(cardId: Long, accountId: Long): RIO[CardRepository, Boolean] = {
    ZIO.serviceWithZIO[CardRepository](_.isCardBelongsToAccount(cardId, accountId))
  }

  def openCard(clientEmail: String, cardType: CardType): RIO[OpenCardEnv, Boolean] =
    ZIO.scoped {
      for {
        clientId <- ZIO.serviceWithZIO[ClientService](_.getIdByEmail(clientEmail))
        accountId <- ZIO.serviceWithZIO[PersonalAccountService](_.getAccountIdByClientId(clientId))
        _ <- ZIO.serviceWithZIO[PersonalAccountService](_.isClientOwnsAccount(clientId, accountId))
          .map(b => if !b then ZIO.fail(Throwable()))
        kafkaRequest <- ZIO.succeed(CardCreateRequest(clientId, clientEmail, accountId, cardType, cardType.defaultCardName))
        metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
        offset <- metadata.map(m => Option(m.offset()))
      } yield offset.isDefined
    }

  def renameCard(clientEmail: String, cardRenameRequest: CardRename): RIO[RenameCardEnv, Boolean] =
    ZIO.scoped {
      for {
        clientId <- ZIO.serviceWithZIO[ClientService](_.getIdByEmail(clientEmail))
        accountId <- ZIO.serviceWithZIO[PersonalAccountService](_.getAccountIdByClientId(clientId))
        _ <- ZIO.serviceWithZIO[PersonalAccountService](_.isClientOwnsAccount(clientId, accountId))
          .map(b => if !b then ZIO.fail(Throwable()))
        kafkaRequest <- ZIO.succeed(CardRenameRequest(clientId, clientEmail, accountId, cardRenameRequest.cardId, cardRenameRequest.newName))
        metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
        offset <- metadata.map(m => Option(m.offset()))
      } yield offset.isDefined
    }

}

object CardService {

  private type OpenCardEnv = ClientService &
    PersonalAccountService &
    ProducerService &
    ClientRepository &
    PersonalAccountRepository

  private type RenameCardEnv = ClientService &
    PersonalAccountService &
    ProducerService &
    ClientRepository &
    PersonalAccountRepository

  private type GetCardsByClientEmailEnv = CardRepository &
    PersonalAccountService &
    PersonalAccountRepository

  val layer: TaskLayer[CardService] = ZLayer.derive[CardService]
}