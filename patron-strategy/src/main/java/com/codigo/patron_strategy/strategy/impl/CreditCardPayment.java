package com.codigo.patron_strategy.strategy.impl;

import com.codigo.patron_strategy.strategy.PaymentStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    @Override
    public void pay(double amount) {
        log.info("PAGANDO EL MONTO DE: " + amount
         + " CON TARJETA DE CREDITO : " + cardNumber
        +" PERTENECIENTE  A: " + cardHolderName);
    }
}
