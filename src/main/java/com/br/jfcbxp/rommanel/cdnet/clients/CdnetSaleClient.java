package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetSaleRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "cdnet-sale-client",
        value = "cdnet-sale-client",
        url = "${cdnet.base-server-url}",
        path = "/api/v2/Sale",
        configuration = CdNetFeignConfig.class
)
public interface CdnetSaleClient {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    String sendSale(
            @RequestHeader("Authorization")
            String token,
            @RequestBody
            List<CdNetSaleRequest> sales);
}
