package com.quotemedia.interview.quoteservice;

import com.quotemedia.interview.quoteservice.dtos.QuoteResponseDTO;
import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.entities.UserEntity;
import com.quotemedia.interview.quoteservice.exceptions.ExceptionResponse;
import com.quotemedia.interview.quoteservice.repositories.QuoteRepository;
import com.quotemedia.interview.quoteservice.repositories.UserRepository;
import com.quotemedia.interview.quoteservice.security.AuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.MessageSource;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SymbolControllerV1IT {

    private static final String URI_API = "/api/v1/symbols/{symbol}/quotes/latest";
    private static final String SYMBOL_GOOG = "GOOG";
    private static final String VALID_SYMBOL_NO_QUOTES = "GOOD";
    private static final String INVALID_SYMBOL_LENGTH_3 = "ABC";
    private static final String INVALID_SYMBOL_LENGTH_7 = "ABCDEFGH";
    private static final BigDecimal BID_VALUE = new BigDecimal("1.2");
    private static final BigDecimal ASK_VALUE = new BigDecimal("2.5");
    private static final String PATH_VARIABLE_SYMBOL = "symbol";
    private static final String QUOTE_NOT_FOUND = "Quote not found.";
    private static final String SYMBOL_LENGTH_ERROR_MESSAGE = "The symbol must be at least 4 characters and at most 6";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private QuoteRepository quoteRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MessageSource messageSource;

    @BeforeEach
    public void init() {
        given(userRepository.findByEmail(anyString())).willReturn(getMockedUserEntity());
    }

    @Test
    public void givenValidSymbolWithQuotes_ThenReturnLatestQuoteSuccessfully() {

        //GIVEN
        Optional<Quote> mockedQuote = getMockedQuote();
        given(quoteRepository.findFirstBySymbolOrderByDayDesc(anyString())).willReturn(mockedQuote);

        Map<String, String> params = new HashMap<>();
        params.put(PATH_VARIABLE_SYMBOL, SYMBOL_GOOG);
        HttpEntity request = getHttpEntityWithJwtTokenInTheHeaders();

        // WHEN
        ResponseEntity<QuoteResponseDTO> exchange = restTemplate.exchange(URI_API, HttpMethod.GET, request,
                QuoteResponseDTO.class, params);

        //THEN
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertEquals(getMockedQuoteResponse(), exchange.getBody());

    }

    @Test
    public void givenInvalidSymbolWith3Chars_thenReturnBadRequestResponse() {

        //GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(SYMBOL_LENGTH_ERROR_MESSAGE);

        Map<String, String> params = new HashMap<>();
        params.put(PATH_VARIABLE_SYMBOL, INVALID_SYMBOL_LENGTH_3);

        HttpEntity request = getHttpEntityWithJwtTokenInTheHeaders();

        // WHEN
        ExceptionResponse response = restTemplate.exchange(URI_API, HttpMethod.GET, request,
                ExceptionResponse.class, params).getBody();

        //THEN
        assertEquals(SYMBOL_LENGTH_ERROR_MESSAGE, response.getMessage());
        assertEquals("uri=/api/v1/symbols/ABC/quotes/latest", response.getDetails());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getHttpErrorCode());

    }

    @Test
    public void givenInvalidSymbolWithMoreThan6Chars_thenReturnBadRequestResponse() {

        //GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(SYMBOL_LENGTH_ERROR_MESSAGE);

        Map<String, String> params = new HashMap<>();
        params.put(PATH_VARIABLE_SYMBOL, INVALID_SYMBOL_LENGTH_7);

        HttpEntity request = getHttpEntityWithJwtTokenInTheHeaders();

        // WHEN
        ExceptionResponse response = restTemplate.exchange(URI_API, HttpMethod.GET, request,
                ExceptionResponse.class, params).getBody();

        //THEN
        assertEquals(SYMBOL_LENGTH_ERROR_MESSAGE, response.getMessage());
        assertEquals("uri=/api/v1/symbols/ABCDEFGH/quotes/latest", response.getDetails());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getHttpErrorCode());

    }

    @Test
    public void givenRequestWithoutAuthorizationToken_thenReturnForbiddenError() {

        //GIVEN
        Map<String, String> params = new HashMap<>();
        params.put(PATH_VARIABLE_SYMBOL, VALID_SYMBOL_NO_QUOTES);

        // WHEN
        ResponseEntity<ExceptionResponse> response = restTemplate.exchange(URI_API, HttpMethod.GET, null,
                ExceptionResponse.class, params);

        //THEN
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode() );

    }

    @Test
    public void givenValidSymbolWithoutQuotes_thenReturnQuoteNotFoundResponse() {

        //GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(QUOTE_NOT_FOUND);

        Map<String, String> params = new HashMap<>();
        params.put(PATH_VARIABLE_SYMBOL, VALID_SYMBOL_NO_QUOTES);

        // WHEN
        HttpEntity request = getHttpEntityWithJwtTokenInTheHeaders();

        // WHEN
        ExceptionResponse response = restTemplate.exchange(URI_API, HttpMethod.GET, request,
                ExceptionResponse.class, params).getBody();

        //THEN
        assertEquals(QUOTE_NOT_FOUND, response.getMessage());
        assertEquals("uri=/api/v1/symbols/GOOD/quotes/latest", response.getDetails());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getHttpErrorCode());

    }

    private Optional<UserEntity> getMockedUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("userId");
        userEntity.setUsername("username");
        userEntity.setEmail("userame@test.com");
        userEntity.setEncryptedPassword("encryptedPassword");
        return Optional.ofNullable(userEntity);
    }

    private QuoteResponseDTO getMockedQuoteResponse() {
        return new QuoteResponseDTO(BID_VALUE, ASK_VALUE);
    }

    private Optional<Quote> getMockedQuote() {
        return Optional.ofNullable(new Quote(SYMBOL_GOOG, LocalDate.now(), BID_VALUE, ASK_VALUE));
    }

    private HttpEntity getHttpEntityWithJwtTokenInTheHeaders() {

        String token = AuthenticationFilter.createToken("username");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity(headers);

    }
}