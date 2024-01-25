package com.br.jfcbxp.rommanel.cdnet.exceptions;


import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class IntegrationErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {

            log.error("IntegrationErrorDecoder.decode - method: {}", methodKey);
            return new IntegrationInternalException(response.reason(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());


        } catch (Exception e) {
            log.error("IntegrationErrorDecoder.decode - Generic error trying to decode exception - method: {} message: {}", methodKey,
                    e.getMessage(), e);
            return new IntegrationInternalException(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
