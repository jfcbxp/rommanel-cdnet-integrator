package com.br.jfcbxp.rommanel.cdnet.configs;

import com.br.jfcbxp.rommanel.cdnet.exceptions.IntegrationErrorDecoder;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
public class CdnetAuthFeignConfig {

    @Bean
    public Decoder feignDecoder() {

        ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;
        return new SpringDecoder(messageConverters);
    }

    @Bean
    @Primary
    @Scope(SCOPE_PROTOTYPE)
    public Encoder feignFormEncoder() {
        return new FormEncoder();
    }

    @Bean
    public IntegrationErrorDecoder errorDecoder() {
        return new IntegrationErrorDecoder();
    }

}