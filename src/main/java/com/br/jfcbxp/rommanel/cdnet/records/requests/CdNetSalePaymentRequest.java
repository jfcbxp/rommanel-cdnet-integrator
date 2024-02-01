package com.br.jfcbxp.rommanel.cdnet.records.requests;

import java.math.BigDecimal;

public record CdNetSalePaymentRequest(String paymentMethod,
                                      BigDecimal value
) {
}
