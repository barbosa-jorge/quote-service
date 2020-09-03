package com.quotemedia.interview.quoteservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponseDTO {
    private BigDecimal bid;
    private BigDecimal ask;
}
