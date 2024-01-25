package com.br.jfcbxp.rommanel.cdnet.records.requests;

import java.math.BigInteger;

public record CdNetInventoryRequest(BigInteger fullProductId,
                                    String identification,
                                    BigInteger quantity,
                                    String lastMoveDate) {
}
