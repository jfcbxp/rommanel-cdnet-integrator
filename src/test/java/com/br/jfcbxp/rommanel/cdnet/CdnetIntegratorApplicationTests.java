package com.br.jfcbxp.rommanel.cdnet;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetAuthClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;

@SpringBootTest
@EnableFeignClients
class CdnetIntegratorApplicationTests {

    @Autowired
    CdnetAuthClient cdnetAuthClient;

    @Test
    void contextLoads2() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "17-ALMEIDA-ELZA");
        params.add("client_secret", "b3e82ac0-64d2-4a57-9879-a1441c0b6b36");
        params.add("username", "REST");
        params.add("password", "A121930@");
        var response = cdnetAuthClient.getToken2(params);

        Assertions.assertNotNull(response);
    }

    @Test
    void contextLoads3() {
        HashMap<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", "17-ALMEIDA-ELZA");
        params.put("client_secret", "b3e82ac0-64d2-4a57-9879-a1441c0b6b36");
        params.put("username", "REST");
        params.put("password", "A121930@");
        var response = cdnetAuthClient.getToken2(params);

        Assertions.assertNotNull(response);
    }

}
