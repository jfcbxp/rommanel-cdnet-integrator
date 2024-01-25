package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetInventoryClient;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetInventoryRequest;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CdNetInventoryServiceImpl implements CdnetInventoryService {

    private static final String ZONE_ID = "UTC-3";
    private static final String INTEGRATION_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private final CdnetInventoryClient client;
    private final CdnetAuthService authService;

    @Value("${cdnet.identification.mario-covas}")
    private String identification;

    @Override
    public void updateInventoryList() {
        var date = ZonedDateTime.now(ZoneId.of(ZONE_ID));
        var integrationDate = date.format(DateTimeFormatter.ofPattern(INTEGRATION_DATE_FORMAT));

        var request = new CdNetInventoryRequest(BigInteger.ONE, identification, BigInteger.ONE, integrationDate);

        client.updateInventoryList(authService.getToken(), List.of(request));

    }
}
