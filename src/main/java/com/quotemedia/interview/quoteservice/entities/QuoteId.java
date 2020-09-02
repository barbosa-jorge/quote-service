package com.quotemedia.interview.quoteservice.entities;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class QuoteId implements Serializable {
    private String symbol;
    private LocalDate day;
}
