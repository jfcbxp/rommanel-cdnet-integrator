package com.br.jfcbxp.rommanel.cdnet.records.requests;

import com.br.jfcbxp.rommanel.cdnet.enums.CdnetSaleTransactionEnum;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetSaleTypeEnum;

import java.util.List;

public record CdNetSaleRequest(String companyIdentification,
                               String orderId,
                               String saleDate,
                               CdnetSaleTransactionEnum transaction,
                               String nfeNumber,
                               String nfekey,
                               List<CdNetSaleProductRequest> saleProducts,
                               List<CdNetSaleTotalRequest> totals,
                               CdnetSaleTypeEnum saleType
) {
}
