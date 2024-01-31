package com.br.jfcbxp.rommanel.cdnet.records.requests;

import java.math.BigInteger;

public record CdNetSaleProductRequest(String fullProductId,
                                      BigInteger quantity
) {
}
