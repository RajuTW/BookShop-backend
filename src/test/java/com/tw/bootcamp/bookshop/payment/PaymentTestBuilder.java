package com.tw.bootcamp.bookshop.payment;

import com.tw.bootcamp.bookshop.purchase.Purchase;

public class PaymentTestBuilder {

    private final Payment.PaymentBuilder paymentBuilder;

    public PaymentTestBuilder(long amount) {
        this.paymentBuilder = Payment.builder()
                .amount(amount)
                .cardSecurityCode(345)
                .creditCardNumber("3566-0020-2036-0505")
                .creditCardExpiration("03/2100");
    }
    public Payment build() {
        return paymentBuilder.build();
    }
}
