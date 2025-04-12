package com.br.jfcbxp.rommanel.cdnet.records.responses.purchase;

public record CdnetPurchaseRecordResponse(
        boolean success,
        CdnetPurchaseDataRecordResponse data
) {


}