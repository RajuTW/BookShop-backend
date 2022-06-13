package com.tw.bootcamp.bookshop.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Payment {
    @JsonProperty
    @Schema(example = "100")
    private long amount;
    @JsonProperty
    @Schema(example = "3566-0020-2036-0505")
    private String creditCardNumber;
    @JsonProperty
    @Schema(example = "03/2100")
    private String creditCardExpiration;
    @JsonProperty
    @Schema(example = "100")
    private int cardSecurityCode;
}
