package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetProductClient;
import com.br.jfcbxp.rommanel.cdnet.clients.ProtheusProductClient;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetProductInfoTypeEnum;
import com.br.jfcbxp.rommanel.cdnet.records.requests.ProtheusProductInfoRequest;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetProductServiceImpl implements CdnetProductService {

    private final CdnetProductClient client;
    private final ProtheusProductClient protheusClient;
    private final CdnetAuthService authService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void syncProducts() {
        log.info("CdNetProductServiceImpl.syncProducts - Start");
        syncProductInfo();
        log.info("CdNetProductServiceImpl.syncProducts - End");

    }

    private void syncProductInfo() {
        log.info("CdNetProductServiceImpl.syncProductInfo - Start");
        var token = authService.getToken();

        for (CdnetProductInfoTypeEnum productInfo : CdnetProductInfoTypeEnum.values()) {
            var response = client.getProductInfo(token, productInfo.getType(), "2021-01-01").data();

            for (Object info : response) {
                protheusClient.sendProductInfo(new ProtheusProductInfoRequest(productInfo.getEndpoint(), info));
            }


        }

        log.info("CdNetProductServiceImpl.syncProductInfo - End");
    }

}
