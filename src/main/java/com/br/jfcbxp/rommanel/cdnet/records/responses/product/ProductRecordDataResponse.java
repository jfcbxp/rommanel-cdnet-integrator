package com.br.jfcbxp.rommanel.cdnet.records.responses.product;

import java.util.List;

public record ProductRecordDataResponse(
        List<Object> produtos,
        int pagina,
        int totalItems,
        int quantidadeItensPorPagina
) {


}