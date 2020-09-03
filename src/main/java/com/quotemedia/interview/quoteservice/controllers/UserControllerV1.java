package com.quotemedia.interview.quoteservice.controllers;

import com.quotemedia.interview.quoteservice.dtos.UserRequestDTO;
import com.quotemedia.interview.quoteservice.dtos.UserResponseDTO;
import com.quotemedia.interview.quoteservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity(userService.save(userRequestDTO), HttpStatus.CREATED);
    }
}