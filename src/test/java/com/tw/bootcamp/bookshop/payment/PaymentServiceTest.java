package com.tw.bootcamp.bookshop.payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    PaymentService paymentService;

    @Test
    void shouldMakePaymentWhenValid(){
        Payment paymentRequest = new PaymentTestBuilder(100).build();
        assertDoesNotThrow(()-> paymentService.makePayment(paymentRequest));
    }

    @Test
    void shouldNotMakePaymentWhenInValid(){
        Payment paymentRequest = new PaymentTestBuilder(-100).build();
        assertThrows(InvalidPaymentException.class, ()->paymentService.makePayment(paymentRequest));
    }
}
