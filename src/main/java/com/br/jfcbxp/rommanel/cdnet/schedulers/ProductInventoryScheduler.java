package com.br.jfcbxp.rommanel.cdnet.schedulers;

import com.br.jfcbxp.rommanel.cdnet.services.CdnetInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductInventoryScheduler {

    private final CdnetInventoryService service;

    @Value("${update-inventory.scheduler.least-lock-time}")
    private String leastLockTimeString;

    @Value("${cdnet.mario-covas.code}")
    private String empresa;

    @Value("${cdnet.mario-covas.warehouse}")
    private String armazem;

    @Scheduled(
            cron = "${update-inventory.scheduler.cron-value}",
            zone = "America/Sao_Paulo"
    )
    @SchedulerLock(
            name = "PRODUCT_INVENTORY",
            lockAtLeastFor = "${update-inventory.scheduler.least-lock-time}",
            lockAtMostFor = "${update-inventory.scheduler.most-lock-time}"
    )
    public void checkOutOfSyncProducts() {
        var start = System.currentTimeMillis();
        try {
            log.info("CdnetInventoryService.checkOutOfSyncProducts - Start");
            service.updateInventory(empresa, armazem);

        } finally {
            log.info("CdnetInventoryService.checkOutOfSyncProducts - End - took [{}ms]", (System.currentTimeMillis() - start));
        }
    }
}