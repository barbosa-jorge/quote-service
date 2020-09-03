package com.quotemedia.interview.quoteservice.dtos;

import lombok.Data;

@Data
public class UserLoginRequestDTO {
    private String email;
    private String password;
}