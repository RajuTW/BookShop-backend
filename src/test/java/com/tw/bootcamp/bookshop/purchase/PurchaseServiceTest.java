package com.tw.bootcamp.bookshop.purchase;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookRepository;
import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserRepository;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import com.tw.bootcamp.bookshop.user.address.CreateAddressRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PurchaseServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AddressService addressService;

    @AfterEach
    void tearDown() {
        purchaseRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreatePurchaseOrderWhenValid() throws OutOfStockException, InvalidBookException, InvalidAddressException {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user);
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);

        CreatePurchaseConfirmationRequest createPurchaseConfirmationRequest = createPurchaseRequest(user.getEmail(), book.getId(), address.getId());
        Purchase purchase = purchaseService.purchaseConfirmation(createPurchaseConfirmationRequest);


        assertNotNull(purchase);
        assertEquals(address.getId(), purchase.getAddress().getId());
        assertEquals(user.getId(), purchase.getUser().getId());
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenInValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user);
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);

        CreatePurchaseConfirmationRequest createInValidRequest = createInvalidPurchaseRequest(user.getEmail(), book.getId(), address.getId());
        assertThrows(ConstraintViolationException.class, () -> purchaseService.purchaseConfirmation(createInValidRequest));
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenUserIsNotValid() {
        CreatePurchaseConfirmationRequest createValidRequest = createPurchaseRequest("testemail@test.com", 1, 1);
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> purchaseService.purchaseConfirmation(createValidRequest));
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenAddressIdIsNotValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user);
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);
        CreatePurchaseConfirmationRequest createValidRequest = createPurchaseRequest(user.getEmail(), book.getId(), address.getId() + 1);
        assertThrows(com.tw.bootcamp.bookshop.purchase.InvalidAddressException.class, () -> purchaseService.purchaseConfirmation(createValidRequest));
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenAddressIdIsDifferentFromUser() {
        User user1 = userRepository.save(new UserTestBuilder().withEmail("testEmail@gmail.com").withId(1).build());
        User user2 = userRepository.save(new UserTestBuilder().withId(2).build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user2);
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);
        CreatePurchaseConfirmationRequest createValidRequest = createPurchaseRequest(user1.getEmail(), book.getId(), address.getId());
        assertThrows(com.tw.bootcamp.bookshop.purchase.InvalidAddressException.class, () -> purchaseService.purchaseConfirmation(createValidRequest));
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenBookIdIsNotValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user);
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);
        CreatePurchaseConfirmationRequest createValidRequest = createPurchaseRequest(user.getEmail(), book.getId()+1, address.getId());
        assertThrows(com.tw.bootcamp.bookshop.purchase.InvalidBookException.class, () -> purchaseService.purchaseConfirmation(createValidRequest));
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenBookIsOutOfStock() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user);
        Book book = new BookTestBuilder().withName("title").withNumberOfAvailableBooks(1).build();
        book = bookRepository.save(book);

        CreatePurchaseConfirmationRequest createValidRequest = createPurchaseRequest(user.getEmail(), book.getId(), address.getId());
        assertThrows(com.tw.bootcamp.bookshop.purchase.OutOfStockException.class, () -> purchaseService.purchaseConfirmation(createValidRequest));
    }

    @Test
    void shouldReduceBookCountWhenOrderIsPlaced() throws OutOfStockException, InvalidBookException, InvalidAddressException {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();
        Address address = addressService.create(createRequest, user);
        Book book = new BookTestBuilder().withName("title").build();
        book = bookRepository.save(book);

        CreatePurchaseConfirmationRequest createPurchaseConfirmationRequest = createPurchaseRequest(user.getEmail(), book.getId(), address.getId());
        purchaseService.purchaseConfirmation(createPurchaseConfirmationRequest);
        book = bookRepository.findById(book.getId()).get();

        assertNotNull(book);
        assertEquals(1, book.getNumberOfAvailableBooks());
    }

    private CreatePurchaseConfirmationRequest createPurchaseRequest(String email, long book_id, long address_id) {
        return CreatePurchaseConfirmationRequest.builder()
                .userEmail(email)
                .bookId(book_id)
                .userAddressId(address_id)
                .quantity(2)
                .paymentMode("COD")
                .build();
    }

    private CreateAddressRequest createAddress() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("GodStone")
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }

    private CreatePurchaseConfirmationRequest createInvalidPurchaseRequest(String email, long book_id, long address_id) {
        return CreatePurchaseConfirmationRequest.builder()
                .userEmail(email)
                .bookId(book_id)
                .userAddressId(address_id)
                .quantity(-2)
                .paymentMode("")
                .build();
    }
}