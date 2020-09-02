package com.quotemedia.interview.quoteservice;

import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.exceptions.BadRequestException;
import com.quotemedia.interview.quoteservice.exceptions.QuoteNotFoundException;
import com.quotemedia.interview.quoteservice.repositories.QuoteRepository;
import com.quotemedia.interview.quoteservice.responses.QuoteResponse;
import com.quotemedia.interview.quoteservice.services.impl.QuoteServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

public class QuoteServiceTest {

    private static final String SYMBOL_EMPTY = "";
    private static final String SYMBOL_NULL = null;
    private static final String SYMBOL_GOOG = "GOOG";
    private static final BigDecimal BID_VALUE = new BigDecimal("1.2");
    private static final BigDecimal ASK_VALUE = new BigDecimal("2.5");
    private static final String SYMBOL_LENGTH_ERROR_MESSAGE = "The symbol must be at least 4 characters and at most 6";
    private static final String INVALID_SYMBOL_LENGTH_3 = "ABC";
    private static final String INVALID_SYMBOL_LENGTH_7 = "ABCDEFG";

    @InjectMocks
    private QuoteServiceImpl quoteService;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenValidSymbolWithQuotes_thenReturnLatestQuoteSuccessfully() {

        // GIVEN
        Quote mockedQuote = getMockedQuote();
        QuoteResponse mockedQuoteResponse = getMockedQuoteResponse();
        given(quoteRepository.findFirstBySymbolOrderByDayDesc(anyString()))
                .willReturn(Optional.ofNullable(mockedQuote));

        //WHEN
        QuoteResponse quoteResponse = this.quoteService.findLatestQuoteBySymbol(SYMBOL_GOOG);

        //THEN
        then(quoteRepository).should().findFirstBySymbolOrderByDayDesc(anyString());
        assertEquals(mockedQuoteResponse.getAsk(), quoteResponse.getAsk());
        assertEquals(mockedQuoteResponse.getBid(), quoteResponse.getBid());
    }

    @Test(expected = QuoteNotFoundException.class)
    public void givenValidSymbolWithNoQuotes_thenReturnQuoteNotFoundException() {

        // GIVEN
        given(quoteRepository.findFirstBySymbolOrderByDayDesc(anyString())).willThrow(QuoteNotFoundException.class);

        //WHEN
        QuoteResponse quoteResponse = this.quoteService.findLatestQuoteBySymbol(SYMBOL_GOOG);

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

    private QuoteResponse getMockedQuoteResponse() {
        return new QuoteResponse(BID_VALUE, ASK_VALUE);
    }
}