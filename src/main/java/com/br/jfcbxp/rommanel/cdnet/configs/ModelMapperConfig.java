package com.br.jfcbxp.rommanel.cdnet.configs;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        final var modelMapper = new ModelMapper();

        return modelMapper;
    }
}
