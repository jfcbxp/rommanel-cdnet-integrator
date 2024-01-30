package com.br.jfcbxp.rommanel.cdnet.records.requests;

import java.math.BigDecimal;

public record CdNetSaleTotalRequest(Integer type,
                                    BigDecimal totalValue
) {
}
