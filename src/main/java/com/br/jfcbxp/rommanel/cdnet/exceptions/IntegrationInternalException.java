package com.br.jfcbxp.rommanel.cdnet.exceptions;

import lombok.Getter;

import java.io.Serial;

@Getter
public class IntegrationInternalException extends RuntimeException {

    private final String message;
    private final Integer status;

    @Serial
    private static final long serialVersionUID = 1L;

    public IntegrationInternalException(String message, Integer status) {
        super(message);
        this.message = message;
        this.status = status;
    }

}
