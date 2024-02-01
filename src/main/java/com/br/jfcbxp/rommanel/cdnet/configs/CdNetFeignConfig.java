package com.br.jfcbxp.rommanel.cdnet.configs;

import com.br.jfcbxp.rommanel.cdnet.exceptions.IntegrationErrorDecoder;
import feign.Logger;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class CdNetFeignConfig {

    @Bean
    public Decoder feignDecoder() {
        ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;
        return new SpringDecoder(messageConverters);
    }

    @Bean
    public IntegrationErrorDecoder errorDecoder() {
        return new IntegrationErrorDecoder();
    }

    @Bean
    @Profile("local")
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}