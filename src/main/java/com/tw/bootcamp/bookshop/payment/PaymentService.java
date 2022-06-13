package com.tw.bootcamp.bookshop.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Service
public class PaymentService {
    final String paymentUri = "https://tw-mock-credit-service.herokuapp.com/payments";

    public void makePayment(@Valid Payment payment) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        String reqBody = mapper.writeValueAsString(payment);

        HttpEntity<String> request = new HttpEntity<>(reqBody, headers);
        try {
            restTemplate.postForObject(paymentUri, request, Object.class);
        } catch (HttpClientErrorException ex) {
            JSONObject json = new JSONObject(ex.getResponseBodyAsString());
            String message = json.optString("details")
                    .replace("[", "").replace("]", "").replace("\"", "");
            throw new InvalidPaymentException(message.isEmpty() ? "Payment Failed" : message);
        }
    }
}
