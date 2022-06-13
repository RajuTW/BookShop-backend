package com.tw.bootcamp.bookshop.purchase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
@WithMockUser
class PurchaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private PurchaseService purchaseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreatePurchaseOrderWhenValid() throws Exception {
        User user = new UserTestBuilder().build();
        Purchase purchase = new PurchaseTestBuilder().build();

        CreatePurchaseConfirmationRequest createPurchaseConfirmationRequest = createPurchaseRequest(user.getEmail(),1);

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(purchaseService.purchaseConfirmation(eq(createPurchaseConfirmationRequest)))
                .thenReturn(purchase);

        mockMvc.perform(post("/purchases")
                        .content(objectMapper.writeValueAsString(createPurchaseConfirmationRequest))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Order PLaced Successfully"));

        verify(purchaseService, times(1))
                .purchaseConfirmation(eq(createPurchaseConfirmationRequest));
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenInValid() throws Exception {

        User user = new UserTestBuilder().build();
        CreatePurchaseConfirmationRequest createPurchaseConfirmationRequest =
                createPurchaseRequest(user.getEmail(), 1);

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(purchaseService.purchaseConfirmation(any())).thenThrow(new ConstraintViolationException(new HashSet<>()));


        mockMvc.perform(post("/purchases")
                .content(objectMapper.writeValueAsString(createPurchaseConfirmationRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"));

        verify(purchaseService, times(1))
                .purchaseConfirmation(eq(createPurchaseConfirmationRequest));
    }

    @Test
    void shouldNotCreateOrderWhenAddressIsInValid() throws Exception {
        CreatePurchaseConfirmationRequest createPurchaseConfirmationRequest = createPurchaseRequest(new UserTestBuilder().build().getEmail(),0);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new UserTestBuilder().build()));
        when(purchaseService.purchaseConfirmation(any())).thenThrow(new InvalidAddressException());

        mockMvc.perform(post("/purchases")
                        .content(objectMapper.writeValueAsString(createPurchaseConfirmationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Address ID does not exist"));

        verify(purchaseService, times(1))
                .purchaseConfirmation(eq(createPurchaseConfirmationRequest));

    }

    private CreatePurchaseConfirmationRequest createPurchaseRequest(String email, long address_id) {
        return CreatePurchaseConfirmationRequest.builder()
                .userEmail(email)
                .bookId(1)
                .userAddressId(address_id)
                .quantity(2)
                .paymentMode("COD")
                .build();
    }

}