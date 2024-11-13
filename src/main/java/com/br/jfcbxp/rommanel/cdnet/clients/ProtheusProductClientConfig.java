package com.br.jfcbxp.rommanel.cdnet.clients;

import feign.Logger;
import feign.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ProtheusProductClientConfig {

    private final String loggerLevel;

    public ProtheusProductClientConfig(
            @Value("${cdnet.loggerLevel}")
            String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(30000, TimeUnit.MILLISECONDS, 30000, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public Logger.Level loggerLevelDefault() {
        return Logger.Level.valueOf(this.loggerLevel);
    }
}
