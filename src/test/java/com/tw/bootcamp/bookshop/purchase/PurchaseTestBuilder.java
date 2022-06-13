package com.tw.bootcamp.bookshop.purchase;

public class PurchaseTestBuilder {

    private final Purchase.PurchaseBuilder purchaseBuilder;

    public PurchaseTestBuilder() {
        this.purchaseBuilder = Purchase.builder()
                .id(10001L);
    }
    public Purchase build() {
        return purchaseBuilder.build();
    }
}
