package com.quotemedia.interview.quoteservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Autowired
    private Environment env;

    public String getTokenSecret() {
        return env.getProperty("tokenSecret");
    }

    public int getMaxEntriesLocalHeap() {
        return Integer.parseInt(env.getProperty("maxEntriesLocalHeap"));
    }

    public int getTimeToLiveSeconds() {
        return Integer.parseInt(env.getProperty("timeToLiveSeconds"));
    }
}
