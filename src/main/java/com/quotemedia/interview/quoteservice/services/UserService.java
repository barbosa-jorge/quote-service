package com.quotemedia.interview.quoteservice.services;

import com.quotemedia.interview.quoteservice.responses.UserRequestDTO;
import com.quotemedia.interview.quoteservice.responses.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponseDTO save(UserRequestDTO userRequestDTO);
    UserResponseDTO update(UserRequestDTO userRequestDTO, String userId);
//    OperationStatusResponse delete(String userId);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO findByUserId(String userId);
}
