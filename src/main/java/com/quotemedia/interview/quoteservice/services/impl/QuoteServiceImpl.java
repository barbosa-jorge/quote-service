package com.quotemedia.interview.quoteservice.services.impl;

import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.exceptions.BadRequestException;
import com.quotemedia.interview.quoteservice.exceptions.QuoteNotFoundException;
import com.quotemedia.interview.quoteservice.repositories.QuoteRepository;
import com.quotemedia.interview.quoteservice.responses.QuoteResponse;
import com.quotemedia.interview.quoteservice.services.QuoteService;
import com.quotemedia.interview.quoteservice.shared.constants.AppQuoteConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class QuoteServiceImpl implements QuoteService {

    private static final int SYMBOL_MIN_LENGTH = 4;
    private static final int SYMBOL_MAX_LENGTH = 6;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private QuoteRepository quoteRepository;

    public QuoteResponse findLatestQuoteBySymbol(String symbol) {

        validateSymbol(symbol);

        Quote quote = this.quoteRepository.findFirstBySymbolOrderByDayDesc(symbol)
                .orElseThrow(() -> new QuoteNotFoundException(messageSource
                        .getMessage(AppQuoteConstants.ERROR_QUOTE_NOT_FOUND, AppQuoteConstants.NO_PARAMS,
                                LocaleContextHolder.getLocale())));

        return new QuoteResponse(quote.getBid(), quote.getAsk());

    }

    private void validateSymbol(String symbol) {
        if (isSymbolOutOfRange(symbol)) {
            throw new BadRequestException(messageSource
                    .getMessage(AppQuoteConstants.ERROR_QUOTE_SYMBOL_LENGTH,
                            new Object[]{SYMBOL_MIN_LENGTH, SYMBOL_MAX_LENGTH}, LocaleContextHolder.getLocale()));

        }
    }

    private boolean isSymbolOutOfRange(String symbol) {
        return symbol.length() < SYMBOL_MIN_LENGTH || symbol.length() > SYMBOL_MAX_LENGTH;
    }

}
