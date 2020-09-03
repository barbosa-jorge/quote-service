package com.quotemedia.interview.quoteservice.services;

import com.quotemedia.interview.quoteservice.dtos.UserRequestDTO;
import com.quotemedia.interview.quoteservice.dtos.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponseDTO save(UserRequestDTO userRequestDTO);
    UserResponseDTO getUserByEmail(String email);
}
