package com.quotemedia.interview.quoteservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponse {
    private BigDecimal bid;
    private BigDecimal ask;
}
