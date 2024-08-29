package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.requests.ProtheusProductInfoRequest;
import com.br.jfcbxp.rommanel.cdnet.records.responses.product.ProductRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "cdnet-product-client",
        value = "cdnet-product-client",
        url = "${cdnet.base-protheus-url}",
        path = "/rest",
        configuration = CdNetFeignConfig.class
)
public interface ProtheusProductClient {

    @PostMapping(
            value = "/estcdnet"
    )
    ProductRecordResponse sendProductInfo(
            @RequestBody()
            ProtheusProductInfoRequest data);
}
