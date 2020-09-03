package com.quotemedia.interview.quoteservice.shared.utils;

import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class Utils {
    public String generateUserId() {
        return UUID.randomUUID().toString();
    }

}
