package com.quotemedia.interview.quoteservice.services;

import com.quotemedia.interview.quoteservice.dtos.QuoteResponseDTO;

public interface QuoteService {
    QuoteResponseDTO findLatestQuoteBySymbol(String symbol);
}
