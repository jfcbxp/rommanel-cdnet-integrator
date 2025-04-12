package com.br.jfcbxp.rommanel.cdnet.records.responses.purchase;

import java.util.List;

public record CdnetPurchaseDataRecordResponse(
        List<CdnetPurchaseBalanceRecordResponse> saldo,
        CdnetPurchasePageRecordResponse paginacao
) {


}