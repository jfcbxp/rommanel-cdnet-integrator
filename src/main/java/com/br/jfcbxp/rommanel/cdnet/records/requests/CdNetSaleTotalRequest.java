package com.br.jfcbxp.rommanel.cdnet.records.requests;

import com.br.jfcbxp.rommanel.cdnet.enums.CdnetSaleTotalTypeEnum;

import java.math.BigDecimal;

public record CdNetSaleTotalRequest(CdnetSaleTotalTypeEnum type,
                                    BigDecimal totalValue
) {
}
