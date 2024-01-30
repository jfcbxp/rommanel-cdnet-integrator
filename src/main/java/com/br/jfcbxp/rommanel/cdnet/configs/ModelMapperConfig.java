package com.br.jfcbxp.rommanel.cdnet.configs;


import com.br.jfcbxp.rommanel.cdnet.converters.ProductInventoryConverter;
import com.br.jfcbxp.rommanel.cdnet.converters.SaleConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Autowired
    private ProductInventoryConverter productInventoryConverter;

    @Autowired
    private SaleConverter saleConverter;

    @Bean
    public ModelMapper modelMapper() {

        final var modelMapper = new ModelMapper();

        modelMapper.addConverter(productInventoryConverter);
        modelMapper.addConverter(saleConverter);

        return modelMapper;
    }
}
