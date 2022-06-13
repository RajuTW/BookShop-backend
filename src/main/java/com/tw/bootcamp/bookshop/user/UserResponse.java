package com.tw.bootcamp.bookshop.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserResponse {
    private final String id;
    private final String email;
    private final String role;

    public UserResponse(User user) {
        this.id = user.getId().toString();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
    }
}
