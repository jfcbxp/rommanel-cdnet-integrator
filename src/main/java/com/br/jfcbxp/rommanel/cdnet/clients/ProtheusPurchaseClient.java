package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.requests.ProtheusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "cdnet-protheus-purchase-client",
        value = "cdnet-protheus-purchase-client",
        url = "${cdnet.base-protheus-url}",
        path = "/rest",
        configuration = CdNetFeignConfig.class
)
public interface ProtheusPurchaseClient {
    Logger log = LoggerFactory.getLogger(ProtheusPurchaseClient.class);

    @PostMapping(
            value = "/estcdnet",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void sendPurchase(
            @RequestBody()
            ProtheusRequest data);

    default void serviceFallbackMethod(ProtheusRequest request, boolean ignoreInvalid, Throwable exception) {
        log.error("ProtheusPurchaseClient.serviceFallbackMethod - error request availability {}", exception.getMessage());
    }
}
