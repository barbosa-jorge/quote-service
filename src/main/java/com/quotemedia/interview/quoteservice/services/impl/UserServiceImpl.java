package com.quotemedia.interview.quoteservice.services.impl;

import com.quotemedia.interview.quoteservice.entities.UserEntity;
import com.quotemedia.interview.quoteservice.exceptions.UserNotFoundException;
import com.quotemedia.interview.quoteservice.repositories.UserRepository;
import com.quotemedia.interview.quoteservice.responses.UserRequestDTO;
import com.quotemedia.interview.quoteservice.responses.UserResponseDTO;
import com.quotemedia.interview.quoteservice.security.UserPrincipal;
import com.quotemedia.interview.quoteservice.services.UserService;
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

    //private Utils utils;

    public UserResponseDTO save(UserRequestDTO userRequestDTO) {

        UserEntity user = modelMapper.map(userRequestDTO, UserEntity.class);
        //user.setUserId(utils.generateUserId());

        // Encrypt password
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    public UserResponseDTO update(UserRequestDTO userRequestDTO, String userId) {

        UserEntity userEntityFound = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException(messageSource
                        .getMessage("error.application.user.not.found", null,
                                LocaleContextHolder.getLocale())));

        modelMapper.map(userRequestDTO, userEntityFound);

        // Encrypt password
        userEntityFound.setEncryptedPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));

        return modelMapper.map(userRepository.save(userEntityFound), UserResponseDTO.class);

    }
//
//    public OperationStatusResponse delete(String userId) {
//
//        UserEntity user = userRepository.findByUserId(userId)
//                .orElseThrow(()-> new UserNotFoundException(messageSource
//                        .getMessage("error.application.user.not.found", null,
//                                LocaleContextHolder.getLocale())));
//
//        userRepository.delete(user);
//
//        OperationStatusResponse response = new OperationStatusResponse();
//        response.setOperationName(RequestOperationName.DELETE.name());
//        response.setOperationStatus(RequestOperationStatus.SUCCESS.name());
//
//        return response;

    //}

    public UserResponseDTO findByUserId(String userId) {

        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException(messageSource
                        .getMessage("error.application.user.not.found", null,
                                LocaleContextHolder.getLocale())));

        return modelMapper.map(user, UserResponseDTO.class);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(messageSource
                        .getMessage("error.application.user.not.found", null,
                                LocaleContextHolder.getLocale())));

        return new UserPrincipal(userEntity);

    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException(messageSource
                        .getMessage("error.application.user.not.found", null,
                                LocaleContextHolder.getLocale())));

        return modelMapper.map(userEntity, UserResponseDTO.class);

    }
}