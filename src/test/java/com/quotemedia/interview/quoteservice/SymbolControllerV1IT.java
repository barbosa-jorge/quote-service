package com.quotemedia.interview.quoteservice;

import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.exceptions.ExceptionResponse;
import com.quotemedia.interview.quoteservice.repositories.QuoteRepository;
import com.quotemedia.interview.quoteservice.responses.QuoteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

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
    private MessageSource messageSource;

    @Test
    public void givenValidSymbolWithQuotes_ThenReturnLatestQuoteSuccessfully() {

        //GIVEN
        Optional<Quote> mockedQuote = getMockedQuote();
        given(quoteRepository.findFirstBySymbolOrderByDayDesc(anyString())).willReturn(mockedQuote);

        Map<String, String> params = new HashMap<>();
        params.put(PATH_VARIABLE_SYMBOL, SYMBOL_GOOG);

        // WHEN
        QuoteResponse response = restTemplate.getForObject(URI_API, QuoteResponse.class, params);

        //THEN
        assertEquals(getMockedQuoteResponse(), response);

    }

    @Test
    public void givenInvalidSymbolWith3Chars_thenReturnBadRequestResponse() {

        //GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(SYMBOL_LENGTH_ERROR_MESSAGE);

        Map<String, String> params = new HashMap<>();
        params.put(PATH_VARIABLE_SYMBOL, "ABC");

        // WHEN
        ExceptionResponse response = restTemplate.getForObject(URI_API, ExceptionResponse.class, params);

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
        params.put(PATH_VARIABLE_SYMBOL, "ABCDEFGH");

        // WHEN
        ExceptionResponse response = restTemplate.getForObject(URI_API, ExceptionResponse.class, params);

        //THEN
        assertEquals(SYMBOL_LENGTH_ERROR_MESSAGE, response.getMessage());
        assertEquals("uri=/api/v1/symbols/ABCDEFGH/quotes/latest", response.getDetails());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getHttpErrorCode());

    }

    @Test
    public void givenValidSymbolWithoutQuotes_thenReturnQuoteNotFoundResponse() {

        //GIVEN
        given(messageSource.getMessage(anyString(), any(), any()))
                .willReturn(QUOTE_NOT_FOUND);

        Map<String, String> params = new HashMap<>();
        params.put(PATH_VARIABLE_SYMBOL, "ABCD");

        // WHEN
        ExceptionResponse response = restTemplate.getForObject(URI_API, ExceptionResponse.class, params);

        //THEN
        assertEquals(QUOTE_NOT_FOUND, response.getMessage());
        assertEquals("uri=/api/v1/symbols/ABCD/quotes/latest", response.getDetails());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getHttpErrorCode());

    }

    private QuoteResponse getMockedQuoteResponse() {
        return new QuoteResponse(BID_VALUE, ASK_VALUE);
    }

    private Optional<Quote> getMockedQuote() {
        return Optional.ofNullable(new Quote(SYMBOL_GOOG, LocalDate.now(), BID_VALUE, ASK_VALUE));
    }
}