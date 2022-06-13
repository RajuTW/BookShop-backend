package com.tw.bootcamp.bookshop.payment;
import com.tw.bootcamp.bookshop.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = {"https://batch-07-team-c-ui.herokuapp.com", "http://localhost:3000"})
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @PostMapping
    @Operation(summary = "Make Payment", description = "Make Payment for Orders", tags = {"Payment"})
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "202",description = "Payment Accepted Successfully ", content = @Content),
                    @ApiResponse(responseCode = "422",description = "Request isn't valid or payment has failed",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))})
            })
    public ResponseEntity<String> create(@RequestBody Payment payment) throws Exception{
        paymentService.makePayment(payment);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
