package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.responses.product.ProductRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "cdnet-product-client",
        value = "cdnet-product-client",
        url = "${cdnet.base-product-url}",
        path = "/api/v1/Produtos",
        configuration = CdNetFeignConfig.class
)
public interface CdnetProductClient {

    @GetMapping(
            value = "/acessorio"
    )
    ProductRecordResponse getProductAccessory(
            @RequestHeader("Authorization")
            String token,
            @RequestParam("dataUltimaAtualizacao")
            String dataUltimaAtualizacao);
}
