package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetAuthClient;
import com.br.jfcbxp.rommanel.cdnet.configs.CdnetProperties;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetAuthServiceImpl implements CdnetAuthService {
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private final CdnetAuthClient client;
    private final CdnetProperties properties;


    @Override
    public String getToken() {
        log.info("CdNetAuthServiceImpl.getToken - Start");

        HashMap<String, String> params = new HashMap<>();

        params.put(GRANT_TYPE, properties.getGrantType());
        params.put(CLIENT_ID, properties.getClientId());
        params.put(CLIENT_SECRET, properties.getClientSecret());
        params.put(USERNAME, properties.getUsername());
        params.put(PASSWORD, properties.getPassword());

        var response = client.getToken(params);

        log.info("CdNetAuthServiceImpl.getToken - End");

        return response.tokenType()
                .concat(" ")
                .concat(response.accessToken());


    }
}
