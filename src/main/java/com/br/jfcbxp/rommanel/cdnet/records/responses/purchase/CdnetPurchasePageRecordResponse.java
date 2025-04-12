package com.br.jfcbxp.rommanel.cdnet.records.responses.purchase;

public record CdnetPurchasePageRecordResponse(
        int quantidadePorPagina,
        int pagina,
        int totalPaginas,
        int totalItens
) {


}