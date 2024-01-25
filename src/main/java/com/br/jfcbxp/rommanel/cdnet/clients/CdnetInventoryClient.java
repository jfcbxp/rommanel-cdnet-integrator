package com.br.jfcbxp.rommanel.cdnet.clients;

import com.br.jfcbxp.rommanel.cdnet.configs.CdNetFeignConfig;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetInventoryRequest;
import com.br.jfcbxp.rommanel.cdnet.records.responses.InventoryRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "cdnet-inventory-client",
        value = "cdnet-inventory-client",
        url = "${cdnet.base-server-url}",
        path = "/api/v1/Inventory",
        configuration = CdNetFeignConfig.class
)
public interface CdnetInventoryClient {

    @PutMapping(
            value = "/UpdateInventoryList",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<InventoryRecordResponse> updateInventoryList(
            @RequestHeader("Authorization")
            String token,
            @RequestBody
            List<CdNetInventoryRequest> inventoryRequests);

    @PutMapping(
            value = "/UpdateInventory",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    InventoryRecordResponse updateInventory(
            @RequestHeader("Authorization")
            String token,
            @RequestBody
            CdNetInventoryRequest inventoryRequests);
}
