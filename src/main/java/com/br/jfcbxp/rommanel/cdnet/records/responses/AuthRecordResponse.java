package com.br.jfcbxp.rommanel.cdnet.records.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthRecordResponse(@JsonProperty("access_token") String accessToken,
                                 @JsonProperty("expires_in") Integer expires,
                                 @JsonProperty("token_type") String tokenType,
                                 @JsonProperty("refresh_token") String refreshToken) {
}
