package com.br.jfcbxp.rommanel.cdnet.records.responses.product;

import java.util.List;

public record ProductInfoRecordResponse(
        boolean success,
        List<Object> data
) {


}