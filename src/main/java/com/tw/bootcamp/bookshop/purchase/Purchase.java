package com.tw.bootcamp.bookshop.purchase;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "purchases")

public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @Min(1)

    private long quantity;
    @Min(1)
    private long total_price;

    @Column(name = "order_date", columnDefinition = "DATE")
    private Date order_date;

    @NotBlank(message = "Payment Method is Mandatory")
    private String payment_mode;

    @NotBlank(message = "Payment Status is mandatory")
    private String payment_status;

    public Purchase() {
    }

    public Purchase(User user, Address address, Book book, long quantity, long total_price, String payment_mode, String payment_status) {
        this.user = user;
        this.address = address;
        this.book = book;
        this.quantity = quantity;
        this.total_price = total_price;
        this.payment_status = payment_status;
        this.payment_mode = payment_mode;
        long milliseconds = System.currentTimeMillis();
        this.order_date = new Date(milliseconds);
    }

    public static Purchase create(User user, Address address, Book book,
                                  CreatePurchaseConfirmationRequest createPurchaseConfirmationRequest) {
        return new Purchase(user, address, book,
                (long) createPurchaseConfirmationRequest.getQuantity(),
                (long) (createPurchaseConfirmationRequest.getQuantity() * book.getPrice().getAmount()),
                createPurchaseConfirmationRequest.getPaymentMode(),
                createPurchaseConfirmationRequest.getPaymentMode() == "COD" ? "PENDING" : "PAID");
    }

    public PurchaseConfirmationResponse toResponse(String message) {
        return PurchaseConfirmationResponse
                .builder()
                .order_id(id)
                .message(message)
                .build();
    }

}
