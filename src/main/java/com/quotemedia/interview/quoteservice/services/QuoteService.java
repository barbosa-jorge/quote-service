package com.quotemedia.interview.quoteservice.services;

import com.quotemedia.interview.quoteservice.dtos.HighestSymbolAskResponseDTO;
import com.quotemedia.interview.quoteservice.dtos.QuoteResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface QuoteService {
    QuoteResponseDTO findLatestQuoteBySymbol(String symbol);
    HighestSymbolAskResponseDTO getHighestSymbolAskByDay(String day);
}
