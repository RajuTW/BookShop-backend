package com.tw.bootcamp.bookshop.purchase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CreatePurchaseConfirmationRequest {
    @NotBlank
    @Schema(example = "test@test.com")
    private String userEmail;
    @NotBlank
    @Schema(example = "2")
    private long userAddressId;
    @NotBlank
    @Schema(example = "2")
    private long bookId;
    @Min(1)
    @Schema(example = "4")
    private int quantity;
    @NotBlank
    @Schema(example = "COD")
    private String paymentMode;
}
