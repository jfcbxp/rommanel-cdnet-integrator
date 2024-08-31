package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "cdnet-product-photo-client",
        value = "cdnet-product-photo-client",
        url = "${cdnet.base-product-photo-url}",
        path = "/api-imagens",
        configuration = CdNetFeignConfig.class
)
public interface CdnetProductPhotoClient {

    @GetMapping("/alta/{productId}")
    Response downloadPhoto(
            @PathVariable("productId")
            String productId);


}
