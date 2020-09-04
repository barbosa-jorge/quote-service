package com.quotemedia.interview.quoteservice.controllers;

import com.quotemedia.interview.quoteservice.dtos.HighestSymbolAskResponseDTO;
import com.quotemedia.interview.quoteservice.dtos.QuoteResponseDTO;
import com.quotemedia.interview.quoteservice.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/symbols")
public class SymbolControllerV1 {

    @Autowired
    private QuoteService quoteService;

    @GetMapping("/{symbol}/quotes/latest")
    public ResponseEntity<QuoteResponseDTO> getLatestQuoteBySymbol(@PathVariable("symbol") String symbol) {
        return new ResponseEntity(this.quoteService.findLatestQuoteBySymbol(symbol), HttpStatus.OK);
    }

    @GetMapping("/highest/ask")
    public ResponseEntity<HighestSymbolAskResponseDTO> getHighestSymbolAskByDay(@RequestParam("day") String day) {
        return new ResponseEntity(this.quoteService.getHighestSymbolAskByDay(day), HttpStatus.OK);
    }
}
