package org.d3javu.backend.services.business;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.kafka.main.business.paymentaccount.PaymentAccountCreateRequest;
import org.d3javu.backend.model.business.paymentaccount.PaymentAccount;
import org.d3javu.backend.repository.business.PaymentAccountRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentAccountService {

    private final PaymentAccountRepository paymentAccountRepository;

    @Async
    public void createPaymentAccount(PaymentAccountCreateRequest request) {
        if(!this.paymentAccountRepository.existsPaymentAccountByClientId(request.clientId())){
            this.paymentAccountRepository.createPaymentAccount(request.clientId());
        }
    }

}
