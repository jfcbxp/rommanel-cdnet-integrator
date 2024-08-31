package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.requests.ProtheusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "cdnet-protheus-product-client",
        value = "cdnet-protheus-product-client",
        url = "${cdnet.base-protheus-url}",
        path = "/rest",
        configuration = CdNetFeignConfig.class
)
public interface ProtheusProductClient {
    Logger log = LoggerFactory.getLogger(ProtheusProductClient.class);

    @PostMapping(
            value = "/estcdnet"
    )
    void sendProductInfo(
            @RequestBody()
            ProtheusRequest data);

    default void serviceFallbackMethod(ProtheusRequest request, boolean ignoreInvalid, Throwable exception) {
        log.error("ProtheusProductClient.serviceFallbackMethod - error request availability {}", exception.getMessage());
    }
}
