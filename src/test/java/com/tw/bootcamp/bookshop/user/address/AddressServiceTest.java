package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserRepository;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;

    @AfterEach
    void tearDown() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAddressWhenValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress("GodStone");

        Address address = addressService.create(createRequest, user);

        assertNotNull(address);
        assertEquals("4 Privet Drive", address.getLineNoOne());
        assertEquals(user.getId(), address.getUser().getId());
    }

    @Test
    void shouldNotCreateAddressWhenInValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress(null);

        assertThrows(ConstraintViolationException.class, ()-> addressService.create(createRequest, user));
    }

    @Test
    void shouldNotCreateAddressWhenUserIsNotValid() {
        CreateAddressRequest createRequest = createAddress("GodStone");
        assertThrows(DataIntegrityViolationException.class, ()-> addressService.create(createRequest, null));
    }


    @Test
    void shouldReturnAddressOfUser() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress("GodStone");

        Address address = addressService.create(createRequest, user);

        List<AddressResponse> addressResponseList = addressService.getListByUser(user);
        assertEquals(1,addressResponseList.size());
        assertEquals("4 Privet Drive", addressResponseList.stream().findFirst().get().getLineNoOne());
    }

    @Test
    void shouldReturnBlankAddressOfNewUser() {
        User user = userRepository.save(new UserTestBuilder().build());

        List<AddressResponse> addressResponseList = addressService.getListByUser(user);
        assertEquals(0,addressResponseList.size());
    }

    private CreateAddressRequest createAddress(String city) {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city(city)
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }



}