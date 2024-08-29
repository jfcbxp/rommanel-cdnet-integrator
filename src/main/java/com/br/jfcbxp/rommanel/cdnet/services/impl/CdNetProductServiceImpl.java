package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetProductClient;
import com.br.jfcbxp.rommanel.cdnet.records.responses.product.ProductAccessoryRecordResponse;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetProductServiceImpl implements CdnetProductService {

    private final CdnetProductClient client;
    private final CdnetAuthService authService;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public void syncProducts() {
        log.info("CdNetProductServiceImpl.syncProducts - Start");
        syncAccessorys();
        log.info("CdNetProductServiceImpl.syncProducts - End");

    }

    private void syncAccessorys() {
        log.info("CdNetProductServiceImpl.syncAccessorys - Start");
        var token = authService.getToken();
        var response = (List<ProductAccessoryRecordResponse>) client.getProductAccessory(token, "2021-01-01").data();
        log.info("CdNetProductServiceImpl.syncAccessorys - End");
    }
}
