package com.br.jfcbxp.rommanel.cdnet.configs;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "${schedulers.default-most-lock-time}")
@EnableFeignClients
public class ApplicationConfig {

}
