package com.quotemedia.interview.quoteservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "QUOTE")
@IdClass(QuoteId.class)
public class Quote {

    @Id
    @Column(name = "SYMBOL")
    private String symbol;

    @Id
    @Column(name = "DAY")
    private LocalDate day;

    @Column(name = "BID")
    private BigDecimal bid;

    @Column(name = "ASK")
    private BigDecimal ask;

}
