package com.tw.bootcamp.bookshop.purchase;

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
@RequestMapping("/purchases")
@CrossOrigin(origins = {"https://batch-07-team-c-ui.herokuapp.com", "http://localhost:3000"})
public class PurchaseController {
    @Autowired
    PurchaseService purchaseService;

    @PostMapping
    @Operation(summary = "Confirm Purchase Order", description = "Confirm Purchase Order for User", tags = {"Purchase Service"})
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "Confirmation created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseConfirmationResponse.class))}),
                    @ApiResponse(responseCode = "422", description = "Request isn't valid or process has failed",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))})
            })
    public ResponseEntity<PurchaseConfirmationResponse> create(@RequestBody CreatePurchaseConfirmationRequest createPurchaseConfirmationRequest) throws OutOfStockException, InvalidBookException, InvalidAddressException {
        Purchase purchase = purchaseService.purchaseConfirmation(createPurchaseConfirmationRequest);
        PurchaseConfirmationResponse purchaseConfirmationResponse = purchase.toResponse("Order PLaced Successfully");
        return new ResponseEntity<>(purchaseConfirmationResponse, HttpStatus.CREATED);
    }

}
