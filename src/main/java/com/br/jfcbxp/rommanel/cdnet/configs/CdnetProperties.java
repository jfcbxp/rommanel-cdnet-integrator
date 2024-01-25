package com.br.jfcbxp.rommanel.cdnet.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "cdnet")
public class CdnetProperties {
    private String grantType;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;


}
