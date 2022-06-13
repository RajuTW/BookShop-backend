package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.payment.InvalidPaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PaymentErrorHandler {
    @ExceptionHandler({ InvalidPaymentException.class })
    public ResponseEntity<ErrorResponse> handleInvalidPaymentError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
