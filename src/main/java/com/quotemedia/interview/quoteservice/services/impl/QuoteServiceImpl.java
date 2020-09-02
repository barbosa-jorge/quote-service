package com.quotemedia.interview.quoteservice.services.impl;

import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.repositories.QuoteRepository;
import com.quotemedia.interview.quoteservice.responses.QuoteResponse;
import com.quotemedia.interview.quoteservice.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    public QuoteResponse findLatestQuoteBySymbol(String symbol) {

        validateSymbol(symbol);

        Quote quote = this.quoteRepository.findFirstBySymbolOrderByDayDesc(symbol)
                .orElseThrow( () -> new RuntimeException("Data Not Found!"));

        return new QuoteResponse(quote.getBid(), quote.getAsk());

    }

    private void validateSymbol(String symbol) {
        if (symbol.length() < 4 || symbol.length() > 6) {
            throw new RuntimeException("The symbol must be at least 4 characters and at most 6");
        }
    }

}
