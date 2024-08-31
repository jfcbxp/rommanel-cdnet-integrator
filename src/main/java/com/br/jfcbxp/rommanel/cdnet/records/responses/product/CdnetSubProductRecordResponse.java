package com.br.jfcbxp.rommanel.cdnet.records.responses.product;

import java.math.BigDecimal;

public record CdnetSubProductRecordResponse(
        String codigoSubproduto,
        String subProduto,
        BigDecimal peso,
        BigDecimal tamanho
) {


}