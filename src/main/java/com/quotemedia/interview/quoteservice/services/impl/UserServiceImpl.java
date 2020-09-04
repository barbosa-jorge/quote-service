package com.quotemedia.interview.quoteservice.services.impl;

import com.quotemedia.interview.quoteservice.dtos.UserRequestDTO;
import com.quotemedia.interview.quoteservice.dtos.UserResponseDTO;
import com.quotemedia.interview.quoteservice.entities.UserEntity;
import com.quotemedia.interview.quoteservice.exceptions.UserNotFoundException;
import com.quotemedia.interview.quoteservice.repositories.UserRepository;
import com.quotemedia.interview.quoteservice.security.UserPrincipal;
import com.quotemedia.interview.quoteservice.services.UserService;
import com.quotemedia.interview.quoteservice.shared.constants.AppQuoteConstants;
import com.quotemedia.interview.quoteservice.shared.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Utils utils;

    @Override
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {

        UserEntity user = modelMapper.map(userRequestDTO, UserEntity.class);
        user.setUserId(utils.generateUserId());

        // Encrypt password
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    /* Internally used by spring security */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(messageSource
                        .getMessage(AppQuoteConstants.ERROR_USER_NOT_FOUND, AppQuoteConstants.NO_PARAMS,
                                LocaleContextHolder.getLocale())));

        return new UserPrincipal(userEntity);

    }

    /* This method is used after success authentication to return a DTO containing the userId */
    @Override
    public UserResponseDTO getUserByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(messageSource
                        .getMessage(AppQuoteConstants.ERROR_USER_NOT_FOUND, AppQuoteConstants.NO_PARAMS,
                                LocaleContextHolder.getLocale())));

        return modelMapper.map(userEntity, UserResponseDTO.class);

    }
}