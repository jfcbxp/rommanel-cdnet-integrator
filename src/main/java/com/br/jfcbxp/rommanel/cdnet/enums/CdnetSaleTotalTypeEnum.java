package com.br.jfcbxp.rommanel.cdnet.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum CdnetSaleTotalTypeEnum {
    FREIGHT, ITEM, DISCOUNT, ADDITIONAL
}
