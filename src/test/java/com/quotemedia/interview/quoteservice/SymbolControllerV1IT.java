package com.quotemedia.interview.quoteservice;

import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.repositories.QuoteRepository;
import com.quotemedia.interview.quoteservice.responses.QuoteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SymbolControllerV1IT {

    private static final String URI_API_GAMES = "/game-management/api/v1/games";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private QuoteRepository quoteRepository;

    @Test
    public void createGame_successfully() {

        Optional<Quote> mockedQuote = getMockedQuote();
        when(quoteRepository.save(any())).thenReturn(mockedQuote);

        QuoteResponse response = restTemplate.postForEntity(URI_API_GAMES, "GOOG", QuoteResponse.class).getBody();

        assertEquals(getMockedQuoteResponse(), response);

    }

    private Optional<Quote> getMockedQuote() {
        return Optional.ofNullable(new Quote());
    }

    private QuoteResponse getMockedQuoteResponse() {
        return new QuoteResponse();
    }

}