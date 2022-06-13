package com.tw.bootcamp.bookshop.user;

import com.tw.bootcamp.bookshop.user.address.AddressResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"https://batch-07-team-c-ui.herokuapp.com", "http://localhost:3000"})
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping ("/signIn")
    @Operation(summary = "Provide User Role", description = "Validate user and provide Role", tags = {"User Service"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Provide User Role",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
                    @ApiResponse(responseCode = "400", content = @Content)
            })
    ResponseEntity<UserResponse> validateCredentials(@RequestBody ValidateUserCredentials userRequest) {
        Optional<User> user = userService.findByEmail(userRequest.getEmail());
        if (user.isPresent()) {
            return new ResponseEntity<>(new UserResponse(user.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Creates user with credentials", tags = {"User Service"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "User created",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))}),
                    @ApiResponse(responseCode = "400", content = @Content),
                    @ApiResponse(responseCode = "422", content = @Content)
            }
    )
    ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest userRequest) throws InvalidEmailException {
        User user = userService.create(userRequest);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update User Details", description = "Update User Details", tags = {"User Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Update User Details",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))})})
    ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) throws UserNotFoundException {
        userService.update(id, updateUserRequest);
        return ResponseEntity.accepted().build();
    }


}
