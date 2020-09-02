package com.quotemedia.interview.quoteservice.controllers;

import com.quotemedia.interview.quoteservice.responses.QuoteResponse;
import com.quotemedia.interview.quoteservice.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @GetMapping("/symbols/{symbol}/quotes/latest")
    public ResponseEntity<QuoteResponse> getLatestQuoteBySymbol(@PathVariable("symbol") String symbol) {
        return new ResponseEntity(this.quoteService.findLatestQuoteBySymbol(symbol), HttpStatus.OK);
    }
}
