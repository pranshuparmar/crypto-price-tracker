package com.pranshu.crypto.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.json.JsonPathBodyFilters;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

import java.util.Collection;
import java.util.HashSet;

@Configuration
public class LogbookConfiguration {

    @Bean
    public Logbook logbook() {
        Collection<Sink> sinkCollection = new HashSet<>();
        Sink logFileSink = new DefaultSink(new JsonHttpLogFormatter(), new DefaultHttpLogWriter());
        sinkCollection.add(logFileSink);
        return Logbook.builder()
                .queryFilter(QueryFilters.replaceQuery("password", "<password>"))
                .bodyFilter(JsonPathBodyFilters.jsonPath("$.password").replace("<password>"))
                .sink(new CompositeSink(sinkCollection))
                .build();
    }

    @Bean
    public LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor() {
        return new LogbookClientHttpRequestInterceptor(logbook());
    }
}
