package com.tw.bootcamp.bookshop.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseConfirmationResponse {
    private Long order_id;
    private String message;
}
