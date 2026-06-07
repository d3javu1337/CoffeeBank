package util

import dao.repository.{AdminRepositoryLive, AuthRepositoryLive, BusinessClientRepositoryLive, ContactPersonRepositoryLive, InvoiceRepositoryLive, PaymentAccountRepositoryLive, PaymentRepositoryLive}
import kafka.messages.businessclient.BusinessClientCreateRequest
import kafka.messages.contactperson.{ContactPersonCreateRequest, ContactPersonDeleteRequest, ContactPersonUpdateRequest}
import kafka.messages.paymentaccount.PaymentAccountCreateRequest
import service.{AdminService, BusinessClientService, ContactPersonService, PaymentAccountService, PaymentService}

type Service = BusinessClientService &
  ContactPersonService &
  PaymentAccountService &
  PaymentService &
  AdminService

type Repository = BusinessClientRepositoryLive &
  ContactPersonRepositoryLive &
  PaymentAccountRepositoryLive &
  PaymentRepositoryLive &
  InvoiceRepositoryLive &
  AdminRepositoryLive &
  AuthRepositoryLive

type KafkaMessage = BusinessClientCreateRequest |
  ContactPersonCreateRequest |
  ContactPersonDeleteRequest |
  ContactPersonUpdateRequest |
  PaymentAccountCreateRequest