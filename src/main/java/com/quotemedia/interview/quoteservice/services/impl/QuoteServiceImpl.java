package com.quotemedia.interview.quoteservice.services.impl;

import com.quotemedia.interview.quoteservice.dtos.HighestSymbolAskResponseDTO;
import com.quotemedia.interview.quoteservice.dtos.QuoteResponseDTO;
import com.quotemedia.interview.quoteservice.entities.Quote;
import com.quotemedia.interview.quoteservice.exceptions.BadRequestException;
import com.quotemedia.interview.quoteservice.exceptions.QuoteNotFoundException;
import com.quotemedia.interview.quoteservice.repositories.QuoteRepository;
import com.quotemedia.interview.quoteservice.services.QuoteService;
import com.quotemedia.interview.quoteservice.shared.constants.AppQuoteConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.DateTimeException;
import java.time.LocalDate;

@Service
public class QuoteServiceImpl implements QuoteService {

    private static final int SYMBOL_MIN_LENGTH = 4;
    private static final int SYMBOL_MAX_LENGTH = 6;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private QuoteRepository quoteRepository;

    /*
    * Caching will be evicted after 1 minute.
    * */
    @Override
    @Cacheable(value = "quotes-cache", key = "'QuotesInCache'+#symbol")
    public QuoteResponseDTO findLatestQuoteBySymbol(String symbol) {

        validateSymbol(symbol);

        Quote quote = this.quoteRepository.findFirstBySymbolOrderByDayDesc(symbol)
                .orElseThrow(() -> new QuoteNotFoundException(messageSource
                        .getMessage(AppQuoteConstants.ERROR_QUOTE_NOT_FOUND, AppQuoteConstants.NO_PARAMS,
                                LocaleContextHolder.getLocale())));

        return new QuoteResponseDTO(quote.getBid(), quote.getAsk());

    }

    /*
     * Caching will be evicted after 1 minute.
     * */
    @Override
    @Cacheable(value = "highestSymbolAsk-cache", key = "'HighestSymbolAskInCache'+#day")
    public HighestSymbolAskResponseDTO getHighestSymbolAskByDay(String day) {

        LocalDate parsedDay = convertStringToLocalDate(day);

        return quoteRepository.findFirstByDayOrderByAskDesc(parsedDay)
                .map(HighestSymbolAskResponseDTO::mapQuoteToResponseDTO)
                .orElseThrow(() -> new QuoteNotFoundException(messageSource
                        .getMessage(AppQuoteConstants.ERROR_QUOTE_NOT_FOUND,
                                AppQuoteConstants.NO_PARAMS, LocaleContextHolder.getLocale())));

    }

    private LocalDate convertStringToLocalDate(String day) {
        try {
            LocalDate parseDay =  LocalDate.parse(day);
            return parseDay;
        } catch (DateTimeException e) {
            throw new BadRequestException(messageSource
                    .getMessage(AppQuoteConstants.ERROR_INVALID_DATE_FORMAT,
                            AppQuoteConstants.NO_PARAMS, LocaleContextHolder.getLocale()));
        }
    }


    private void validateSymbol(String symbol) {
        if (isSymbolOutOfRange(symbol)) {
            throw new BadRequestException(messageSource
                    .getMessage(AppQuoteConstants.ERROR_QUOTE_SYMBOL_LENGTH,
                            new Object[]{SYMBOL_MIN_LENGTH, SYMBOL_MAX_LENGTH},
                                LocaleContextHolder.getLocale()));

        }
    }

    private boolean isSymbolOutOfRange(String symbol) {
        return StringUtils.isEmpty(symbol)
                || symbol.trim().length() < SYMBOL_MIN_LENGTH
                  || symbol.trim().length() > SYMBOL_MAX_LENGTH;
    }
}
