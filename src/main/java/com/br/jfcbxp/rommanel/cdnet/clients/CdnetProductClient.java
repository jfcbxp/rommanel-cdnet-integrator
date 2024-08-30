package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.responses.product.ProductInfoRecordResponse;
import com.br.jfcbxp.rommanel.cdnet.records.responses.product.ProductRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(
        name = "cdnet-product-client",
        value = "cdnet-product-client",
        url = "${cdnet.base-product-url}",
        path = "/api/v1/Produtos",
        configuration = CdNetFeignConfig.class
)
public interface CdnetProductClient {

    @GetMapping()
    ProductRecordResponse getProducts(
            @RequestHeader("Authorization")
            String token,
            @RequestParam("QuantidadePorPagina")
            int pageSize,
            @RequestParam("NumeroPagina")
            int page,
            @RequestParam("dataUltimaAtualizacao")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate lastSync);

    @GetMapping(
            value = "/{type}"
    )
    ProductInfoRecordResponse getProductsInfo(
            @RequestHeader("Authorization")
            String token,
            @PathVariable("type")
            String type,
            @RequestParam("dataUltimaAtualizacao")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate lastSync);

}
