package com.quotemedia.interview.quoteservice.dtos;

import com.quotemedia.interview.quoteservice.entities.Quote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighestSymbolAskResponseDTO {

    private String symbol;
    private BigDecimal ask;

    public static HighestSymbolAskResponseDTO mapEntityToResponseDTO(Quote quote) {
        return new HighestSymbolAskResponseDTO(quote.getSymbol(), quote.getAsk());
    }

}
