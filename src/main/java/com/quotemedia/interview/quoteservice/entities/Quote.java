package com.quotemedia.interview.quoteservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return Objects.equals(symbol, quote.symbol) &&
                Objects.equals(day, quote.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, day);
    }
}
