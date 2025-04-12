package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.responses.purchase.CdnetPurchaseRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "cdnet-purchase-client",
        value = "cdnet-purchase-client",
        url = "${cdnet.base-purchase-url}",
        path = "/api/v1/PedidoDistribuidor",
        configuration = CdNetFeignConfig.class
)
public interface CdnetPurchaseClient {

    @GetMapping(
            value = "/Saldo"
    )
    CdnetPurchaseRecordResponse getAvailablePurchaseBalance(
            @RequestHeader("Authorization")
            String token,
            @RequestParam("CNPJ")
            String identification,
            @RequestParam("Pagina")
            int page,
            @RequestParam("QuantidadePorPagina")
            int pageSize
    );
}
