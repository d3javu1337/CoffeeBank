package org.d3javu.backend.repository.business;

import org.d3javu.backend.model.business.paymentaccount.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {
}
