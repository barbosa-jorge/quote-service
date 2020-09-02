package com.quotemedia.interview.quoteservice.services;

import com.quotemedia.interview.quoteservice.responses.QuoteResponse;

public interface QuoteService {
    QuoteResponse findLatestQuoteBySymbol(String symbol);
}
