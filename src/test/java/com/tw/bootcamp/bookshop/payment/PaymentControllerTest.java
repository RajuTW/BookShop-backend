package com.tw.bootcamp.bookshop.payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@WithMockUser
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldMakePaymentWhenValid() throws Exception {
        Payment paymentRequest = new PaymentTestBuilder(100).build();
        mockMvc.perform(post("/payment")
                .content(objectMapper.writeValueAsString(paymentRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted());
        verify(paymentService, times(1))
                .makePayment(eq(paymentRequest));
    }

    @Test
    void shouldMakePaymentWhenInValid() throws Exception {
        Payment paymentRequest = new PaymentTestBuilder(-100).build();
        doThrow(new InvalidPaymentException("Amount must be more than 0.1")).when(paymentService).makePayment(any());
        mockMvc.perform(post("/payment")
                .content(objectMapper.writeValueAsString(paymentRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message")
                        .value("Amount must be more than 0.1"));
        verify(paymentService, times(1))
                .makePayment(eq(paymentRequest));
    }

}
