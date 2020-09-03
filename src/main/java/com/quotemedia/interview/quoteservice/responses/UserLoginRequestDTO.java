package com.quotemedia.interview.quoteservice.responses;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String email;
    private String password;
}