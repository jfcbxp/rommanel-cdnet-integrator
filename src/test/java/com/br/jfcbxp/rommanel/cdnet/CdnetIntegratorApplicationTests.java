package com.br.jfcbxp.rommanel.cdnet;

import com.br.jfcbxp.rommanel.cdnet.services.impl.CdNetInventoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootTest
@EnableFeignClients
class CdnetIntegratorApplicationTests {

    @Autowired
    CdNetInventoryServiceImpl inventoryService;

    @Test
    void contextLoads() {
        inventoryService.updateInventoryList();
    }


}
