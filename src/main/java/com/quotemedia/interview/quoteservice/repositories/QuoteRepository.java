package com.quotemedia.interview.quoteservice.repositories;

import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.entities.QuoteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, QuoteId> {
    Optional<Quote> findFirstBySymbolOrderByDayDesc(String symbol);
}
