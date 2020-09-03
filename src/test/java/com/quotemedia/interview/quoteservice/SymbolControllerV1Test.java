package com.quotemedia.interview.quoteservice;

import com.quotemedia.interview.quoteservice.controllers.SymbolControllerV1;
import com.quotemedia.interview.quoteservice.exceptions.BadRequestException;
import com.quotemedia.interview.quoteservice.exceptions.QuoteNotFoundException;
import com.quotemedia.interview.quoteservice.dtos.QuoteResponseDTO;
import com.quotemedia.interview.quoteservice.services.QuoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SymbolControllerV1.class)
public class SymbolControllerV1Test {

    private static final String URI_API_SYMBOLS = "/api/v1/symbols/{symbol}/quotes/latest";
    private static final String BID_VALUE = "0.67";
    private static final String ASK_VALUE = "0.81";
    private static final String SYMBOL_GOOG = "GOOG";
    private static final String VALID_SYMBOL_NO_QUOTES = "GOOD";
    private static final String INVALID_SYMBOL_LENGTH_3 = "ABC";
    private static final String INVALID_SYMBOL_LENGTH_7 = "ABCDEFG";
    private static final String INVALID_SYMBOL_EMPTY_SPACE = " ";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private QuoteService quoteService;

    @Test
    public void givenValidSymbolWithQuotes_thenReturnLatestQuoteSuccessfully() throws Exception {

        QuoteResponseDTO quoteResponseDTO = createQuoteResponse(BID_VALUE, ASK_VALUE);
        given(quoteService.findLatestQuoteBySymbol(anyString())).willReturn(quoteResponseDTO);

        mockMvc.perform(get(URI_API_SYMBOLS, SYMBOL_GOOG))
                .andExpect(status().isOk())
                .andExpect(content().json(getMockedQuoteResponse()))
                .andReturn();

    }

    @Test
    public void givenInvalidEmptySymbol_thenReturnBadRequestResponse() throws Exception {

        given(quoteService.findLatestQuoteBySymbol(anyString())).willThrow(BadRequestException.class);

        mockMvc.perform(get(URI_API_SYMBOLS, INVALID_SYMBOL_EMPTY_SPACE))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void givenInvalidSymbolWith3Chars_thenReturnBadRequestResponse() throws Exception {

        given(quoteService.findLatestQuoteBySymbol(anyString())).willThrow(BadRequestException.class);

        mockMvc.perform(get(URI_API_SYMBOLS, INVALID_SYMBOL_LENGTH_3))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void givenInvalidSymbolWithMoreThan6Chars_thenReturnBadRequestResponse() throws Exception {

        given(quoteService.findLatestQuoteBySymbol(anyString())).willThrow(BadRequestException.class);

        mockMvc.perform(get(URI_API_SYMBOLS, INVALID_SYMBOL_LENGTH_7))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void givenValidSymbolWithNoQuotes_thenReturnQuoteNotFoundResponse() throws Exception {

        given(quoteService.findLatestQuoteBySymbol(anyString())).willThrow(QuoteNotFoundException.class);

        mockMvc.perform(get(URI_API_SYMBOLS, VALID_SYMBOL_NO_QUOTES))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    private QuoteResponseDTO createQuoteResponse(String bid, String ask) {
        return new QuoteResponseDTO(new BigDecimal(bid), new BigDecimal(ask));
    }

    private String getMockedQuoteResponse() {
        return "{ bid: 0.67, ask: 0.81 }";
    }
}