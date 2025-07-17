package util

import dao.repository.{BusinessClientRepositoryLive, ContactPersonRepositoryLive, PaymentAccountRepositoryLive, PaymentRepositoryLive}
import kafka.messages.businessclient.BusinessClientCreateRequest
import kafka.messages.contactperson.{ContactPersonCreateRequest, ContactPersonDeleteRequest, ContactPersonUpdateRequest}
import kafka.messages.paymentaccount.PaymentAccountCreateRequest
import service.{BusinessClientService, ContactPersonService, PaymentAccountService, PaymentService}

type Service = BusinessClientService &
  ContactPersonService &
  PaymentAccountService &
  PaymentService

type Repository = BusinessClientRepositoryLive &
  ContactPersonRepositoryLive &
  PaymentAccountRepositoryLive &
  PaymentRepositoryLive

type KafkaMessage = BusinessClientCreateRequest |
  ContactPersonCreateRequest |
  ContactPersonDeleteRequest |
  ContactPersonUpdateRequest |
  PaymentAccountCreateRequest