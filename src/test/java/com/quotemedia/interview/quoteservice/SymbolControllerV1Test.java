package com.quotemedia.interview.quoteservice;

import com.quotemedia.interview.quoteservice.responses.QuoteResponse;
import com.quotemedia.interview.quoteservice.services.QuoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SymbolControllerV1Test.class)
public class SymbolControllerV1Test {

    private static final String URI_API_SYMBOLS = "/api/v1/symbols/{symbol}/quotes/latest";
    private static final String BID_VALUE = "0.67";
    private static final String ASK_VALUE = "0.81";
    private static final String SYMBOL_GOOG = "GOOG";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteService;

    @Test
    public void createGame_successfully() throws Exception {

        QuoteResponse quoteResponse = createQuoteResponse(BID_VALUE, ASK_VALUE);
        when(quoteService.findLatestQuoteBySymbol(anyString())).thenReturn(quoteResponse);
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(URI_API_SYMBOLS)
//                .content(SYMBOL_GOOG)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(get(URI_API_SYMBOLS, SYMBOL_GOOG))
                .andExpect(status().isOk())
                .andExpect(content().json(getMockedQuoteResponse()))
                .andReturn();

    }

    private QuoteResponse createQuoteResponse(String bid, String ask) {
        return new QuoteResponse(new BigDecimal(bid), new BigDecimal(ask));
    }

    private String getMockedQuoteResponse() {
        return "{ bid: 0.67, ask: 0.81 }";
    }
}