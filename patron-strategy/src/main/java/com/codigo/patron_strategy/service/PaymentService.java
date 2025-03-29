package com.codigo.patron_strategy.service;

import com.codigo.patron_strategy.strategy.PaymentStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PaymentService {

    private PaymentStrategy paymentStrategy;
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void processPayment(double amount){

        if (paymentStrategy == null) throw new IllegalStateException("No hay estrategias seleccionadas");

        paymentStrategy.pay(amount);


    }
}
