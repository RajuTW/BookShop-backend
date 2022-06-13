package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.purchase.InvalidAddressException;
import com.tw.bootcamp.bookshop.purchase.InvalidBookException;
import com.tw.bootcamp.bookshop.purchase.OutOfStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PurchaseErrorHandler {
    @ExceptionHandler({ InvalidBookException.class })
    public ResponseEntity<ErrorResponse> handleInvalidBookIdError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ InvalidAddressException.class })
    public ResponseEntity<ErrorResponse> handleInvalidAddressError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ OutOfStockException.class })
    public ResponseEntity<ErrorResponse> handleOutOfStockError(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
