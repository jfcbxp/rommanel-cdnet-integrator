package com.br.jfcbxp.rommanel.cdnet.records.responses;

public record InventoryRecordResponse(boolean success,
                                      Integer statusCode,
                                      String message,
                                      String data) {
}
