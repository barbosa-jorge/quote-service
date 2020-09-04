package com.quotemedia.interview.quoteservice;

import com.quotemedia.interview.quoteservice.dtos.QuoteResponseDTO;
import com.quotemedia.interview.quoteservice.dtos.UserRequestDTO;
import com.quotemedia.interview.quoteservice.dtos.UserResponseDTO;
import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.entities.UserEntity;
import com.quotemedia.interview.quoteservice.exceptions.BadRequestException;
import com.quotemedia.interview.quoteservice.exceptions.QuoteNotFoundException;
import com.quotemedia.interview.quoteservice.exceptions.UserNotFoundException;
import com.quotemedia.interview.quoteservice.repositories.QuoteRepository;
import com.quotemedia.interview.quoteservice.repositories.UserRepository;
import com.quotemedia.interview.quoteservice.security.UserPrincipal;
import com.quotemedia.interview.quoteservice.services.impl.QuoteServiceImpl;
import com.quotemedia.interview.quoteservice.services.impl.UserServiceImpl;
import com.quotemedia.interview.quoteservice.shared.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

public class UserServiceTest {

    private static final String SYMBOL_EMPTY = "";
    private static final String SYMBOL_NULL = null;
    private static final String SYMBOL_GOOG = "GOOG";
    private static final BigDecimal BID_VALUE = new BigDecimal("1.2");
    private static final BigDecimal ASK_VALUE = new BigDecimal("2.5");
    private static final String SYMBOL_LENGTH_ERROR_MESSAGE = "The symbol must be at least 4 characters and at most 6";
    private static final String INVALID_SYMBOL_LENGTH_3 = "ABC";
    private static final String INVALID_SYMBOL_LENGTH_7 = "ABCDEFG";

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuoteServiceImpl quoteService;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private Utils utils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    //UserDetails loadUserByUsername(String email)
    //UserResponseDTO getUserByEmail(String email)
    //UserResponseDTO save(UserRequestDTO userRequestDTO)

    @Test
    public void givenValidEmail_thenReturnUserResponseDTOSuccessfully() {

        // GIVEN
        String validEmail = "test@test.com";
        UserEntity mockedUserEntity = getMockedUserEntity();
        UserResponseDTO expectedResponseDTO = getMockedUserResponseDTO();

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(mockedUserEntity));

        given(modelMapper.map(any(), any()))
                .willReturn(expectedResponseDTO);

        //WHEN
        UserResponseDTO responseDTO = userService.getUserByEmail(validEmail);

        //THEN
        then(userRepository).should().findByEmail(anyString());
        assertEquals(expectedResponseDTO, responseDTO);

    }

    @Test
    public void givenValidUsername_thenReturnUserDetailsSuccessfully() {

        // GIVEN
        String validUsername = "username";
        UserEntity mockedUserEntity = getMockedUserEntity();
        UserDetails mockedUserDetails = new UserPrincipal(mockedUserEntity);

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(mockedUserEntity));

        //WHEN
        UserDetails userDetails = userService.loadUserByUsername(validUsername);

        //THEN
        then(userRepository).should().findByEmail(anyString());
        assertEquals(mockedUserDetails, userDetails);

    }

    @Test
    public void givenValidUserRequestDTO_thenCreateUserSuccessfully() {

        //GIVEN
        UserRequestDTO userRequestDTO = getMockedUserRequestDTO();
        UserResponseDTO expectedUserResponseDTO = getMockedUserResponseDTO();
        UserEntity mockedUserEntity = getMockedUserEntity();

        given(utils.generateUserId()).willReturn("userId");
        given(bCryptPasswordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(modelMapper.map(userRequestDTO, UserEntity.class))
                .willReturn(mockedUserEntity);
        given(modelMapper.map(mockedUserEntity, UserResponseDTO.class))
                .willReturn(expectedUserResponseDTO);
        given(userRepository.save(any()))
                .willReturn(mockedUserEntity);

        //WHEN
        UserResponseDTO userResponseDTO = userService.save(userRequestDTO);

        //THEN
        then(userRepository).should().save(any());
        assertEquals(expectedUserResponseDTO, userResponseDTO);

    }

    @Test(expected = BadRequestException.class)
    public void givenExistentUser_thenReturnErrorUserAlreadyExists() {

        //GIVEN
        UserRequestDTO userRequestDTO = getMockedUserRequestDTO();
        UserResponseDTO expectedUserResponseDTO = getMockedUserResponseDTO();
        UserEntity mockedUserEntity = getMockedUserEntity();

        given(utils.generateUserId()).willReturn("userId");
        given(bCryptPasswordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(modelMapper.map(userRequestDTO, UserEntity.class))
                .willReturn(mockedUserEntity);
        given(modelMapper.map(mockedUserEntity, UserResponseDTO.class))
                .willReturn(expectedUserResponseDTO);
        given(userRepository.save(any()))
                .willThrow(BadRequestException.class);

        //WHEN
        UserResponseDTO userResponseDTO = userService.save(userRequestDTO);

        //THEN
        then(userRepository).should().save(any());

    }

    @Test(expected = UserNotFoundException.class)
    public void givenInvalidUsername_thenReturnUserNotFoundException() {

        // GIVEN
        String invalidEmail = "nonexistentemail@test.com";
        given(userRepository.findByEmail(anyString())).willThrow(UserNotFoundException.class);

        //WHEN
        this.userService.getUserByEmail(invalidEmail);

        //THEN
        then(userRepository).should().findByEmail(anyString());
    }

    @Test(expected = UserNotFoundException.class)
    public void givenInvalidUsernameWhenLoadUserByUsername_thenReturnUserNotFoundException() {

        // GIVEN
        String invalidEmail = "nonexistentemail@test.com";
        given(userRepository.findByEmail(anyString())).willThrow(UserNotFoundException.class);

        //WHEN
        this.userService.loadUserByUsername(invalidEmail);

        //THEN
        then(userRepository).should().findByEmail(anyString());
    }

    private UserRequestDTO getMockedUserRequestDTO () {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@test.com");
        userRequestDTO.setPassword("12345678");
        userRequestDTO.setUsername("username");
        return userRequestDTO;
    }

    private UserResponseDTO getMockedUserResponseDTO() {
        UserResponseDTO expectedResponseDTO = new UserResponseDTO();
        expectedResponseDTO.setEmail("test@test.com");
        expectedResponseDTO.setUserId("userId");
        expectedResponseDTO.setUserName("username");
        return expectedResponseDTO;
    }

    private UserEntity getMockedUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setUsername("username");
        userEntity.setEncryptedPassword("encryptedPassword");
        userEntity.setUserId("userId");
        return userEntity;
    }




    @Test(expected = QuoteNotFoundException.class)
    public void givenValidSymbolWithNoQuotes_thenReturnQuoteNotFoundException() {

        // GIVEN
        given(quoteRepository.findFirstBySymbolOrderByDayDesc(anyString())).willThrow(QuoteNotFoundException.class);

        //WHEN
        QuoteResponseDTO quoteResponseDTO = this.quoteService.findLatestQuoteBySymbol(SYMBOL_GOOG);

        //THEN
        then(quoteRepository).should().findFirstBySymbolOrderByDayDesc(anyString());
    }

    @Test(expected = BadRequestException.class)
    public void givenNullSymbol_thenReturnBadRequestException() {

        // GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(SYMBOL_LENGTH_ERROR_MESSAGE);

        //WHEN
        this.quoteService.findLatestQuoteBySymbol(SYMBOL_NULL);

        //THEN
        then(quoteRepository).should(never()).findFirstBySymbolOrderByDayDesc(anyString());
    }

    @Test(expected = BadRequestException.class)
    public void givenEmptySymbol_thenReturnBadRequestException() {

        // GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(SYMBOL_LENGTH_ERROR_MESSAGE);

        //WHEN
        this.quoteService.findLatestQuoteBySymbol(SYMBOL_EMPTY);

        //THEN
        then(quoteRepository).should(never()).findFirstBySymbolOrderByDayDesc(anyString());
    }

    @Test(expected = BadRequestException.class)
    public void givenInvalidSymbolWith3Chars_thenReturnBadRequestException() {

        // GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(SYMBOL_LENGTH_ERROR_MESSAGE);

        //WHEN
        this.quoteService.findLatestQuoteBySymbol(INVALID_SYMBOL_LENGTH_3);

        //THEN
        then(quoteRepository).should(never()).findFirstBySymbolOrderByDayDesc(anyString());
    }

    @Test(expected = BadRequestException.class)
    public void givenInvalidSymbolWithMoreThan6Chars_thenReturnBadRequestException() {

        // GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(SYMBOL_LENGTH_ERROR_MESSAGE);

        //WHEN
        this.quoteService.findLatestQuoteBySymbol(INVALID_SYMBOL_LENGTH_7);

        //THEN
        then(quoteRepository).should(never()).findFirstBySymbolOrderByDayDesc(anyString());
    }

    private Quote getMockedQuote() {
        return new Quote(SYMBOL_GOOG, LocalDate.now(), BID_VALUE, ASK_VALUE);
    }

    private QuoteResponseDTO getMockedQuoteResponse() {
        return new QuoteResponseDTO(BID_VALUE, ASK_VALUE);
    }
}