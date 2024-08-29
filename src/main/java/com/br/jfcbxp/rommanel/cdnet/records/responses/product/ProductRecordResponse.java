package com.br.jfcbxp.rommanel.cdnet.records.responses.product;

public record ProductRecordResponse<T>(
        boolean success,
        T data
) {


}