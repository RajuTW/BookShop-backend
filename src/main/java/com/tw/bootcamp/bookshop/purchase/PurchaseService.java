package com.tw.bootcamp.bookshop.purchase;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PurchaseService {
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    BookService bookService;
    @Autowired
    AddressService addressService;
    @Autowired
    UserService userService;

    public Purchase purchaseConfirmation(@Valid CreatePurchaseConfirmationRequest createPurchaseConfirmationRequest) throws OutOfStockException, InvalidBookException, InvalidAddressException {
        User user = User(createPurchaseConfirmationRequest.getUserEmail());
        Optional<Book> book = Book(createPurchaseConfirmationRequest.getBookId());
        Optional<Address> address = Address(createPurchaseConfirmationRequest.getUserAddressId() , user.getId() );
        Purchase purchase = Purchase.create(user, address.get(), book.get(), createPurchaseConfirmationRequest);
        reduceBookCount(book.get(),createPurchaseConfirmationRequest.getQuantity());
        return purchaseRepository.save(purchase);
    }

    private User User(String userEmail) {
        return userService.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Optional<Address> Address(Long addressId , Long userId) throws InvalidAddressException {
        Optional<Address> address = addressService.fetchAll()
                .stream()
                .filter(address1 -> Objects.equals(address1.getId(), addressId)
                        & Objects.equals(address1.getUser().getId(), userId))
                .findFirst();
        if (!address.isPresent()) {
            throw new InvalidAddressException();
        }
        return address;
    }

    private Optional<Book> Book(Long id) throws InvalidBookException {
        Optional<Book> book = bookService.fetchAll()
                .stream()
                .filter(book1 -> Objects.equals(book1.getId(), id))
                .findFirst();
        if (!book.isPresent()) {
            throw new InvalidBookException();
        }
        return book;
    }

    public void reduceBookCount(Book book, int quantity) throws OutOfStockException {
        int availableQuantity = book.getNumberOfAvailableBooks()-quantity;
        if(availableQuantity<0){
            throw new OutOfStockException();
        }
        book.setNumberOfAvailableBooks(availableQuantity);
        bookService.saveBook(book);
    }
    public List<Purchase> fetchAll() {
        return purchaseRepository.findAll();
    }
}
