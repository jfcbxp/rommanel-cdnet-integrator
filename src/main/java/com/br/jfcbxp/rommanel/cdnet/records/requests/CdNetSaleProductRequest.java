package com.br.jfcbxp.rommanel.cdnet.records.requests;

import java.math.BigDecimal;
import java.math.BigInteger;

public record CdNetSaleProductRequest(String fullProductId,
                                      BigInteger quantity,
                                      BigDecimal totalValue
) {
}
