package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.responses.AuthRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "cdnet-auth-client",
        value = "cdnet-auth-client",
        url = "${cdnet.authentication-server-url}",
        path = "/connect",
        configuration = CdNetFeignConfig.class
)
public interface CdnetAuthClient {

    @PostMapping(
            value = "/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    AuthRecordResponse getToken(
            @RequestBody
            Map<String, ?> params);
}
