package com.quotemedia.interview.quoteservice.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserRequestDTO {

    @NotBlank(message = "{error.application.username.not.blank}")
    @Size(min = 3, max = 100, message = "{error.application.username.min.max.size}")
    private String username;

    @NotBlank(message = "{error.application.email.not.blank}")
    @Email(message = "{error.application.invalid.email}")
    private String email;

    @NotBlank(message = "{error.application.password.not.blank}")
    @Size(min = 8, max = 16, message = "{error.application.password.min.max.size}")
    private String password;
}