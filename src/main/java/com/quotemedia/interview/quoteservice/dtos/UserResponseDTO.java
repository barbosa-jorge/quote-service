package com.quotemedia.interview.quoteservice.dtos;

import lombok.Data;

@Data
public class UserResponseDTO {

    private String userId;
    private String userName;
    private String email;

}