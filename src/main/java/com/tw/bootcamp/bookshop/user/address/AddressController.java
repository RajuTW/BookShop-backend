package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/addresses")
@CrossOrigin(origins = {"https://batch-07-team-c-ui.herokuapp.com", "http://localhost:3000"})
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Create address", description = "Creates address for user", tags = {"Address Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Address created",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AddressResponse.class))})})
    public ResponseEntity<AddressResponse> create(@RequestBody CreateAddressRequest createRequest, Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Address address = addressService.create(createRequest, user);
        AddressResponse addressResponse = address.toResponse();
        return new ResponseEntity<>(addressResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{email}")
    @Operation(summary = "Get Address List", description = "List all address tagged to the signed-in user", tags = {"Address Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "List All Address",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponse.class))})})
    public List<AddressResponse> getAddressList(@PathVariable String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return addressService.getListByUser(user);
    }
}
