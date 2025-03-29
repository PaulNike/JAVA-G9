package com.codigo.patron_strategy.strategy.impl;

import com.codigo.patron_strategy.strategy.PaymentStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class PayPalPayment implements PaymentStrategy {
    private String email;
    @Override
    public void pay(double amount) {
        log.info("PAGANDO EL MONTO DE: " + amount
                + " CON PAYPAL SERVICE PERTENECIENTE  A: " + email);
    }
}
