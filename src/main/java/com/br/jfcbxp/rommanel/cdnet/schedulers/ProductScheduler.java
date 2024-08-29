package com.br.jfcbxp.rommanel.cdnet.schedulers;

import com.br.jfcbxp.rommanel.cdnet.services.CdnetProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductScheduler {

    private final CdnetProductService service;

    @Scheduled(
            cron = "${update-product.scheduler.cron-value}",
            zone = "America/Sao_Paulo"
    )
    @SchedulerLock(
            name = "PRODUCT",
            lockAtLeastFor = "${update-product.scheduler.least-lock-time}",
            lockAtMostFor = "${update-product.scheduler.most-lock-time}"
    )
    public void checkOutOfSyncProducts() {
        var start = System.currentTimeMillis();
        try {
            log.info("ProductScheduler.checkOutOfSyncProducts - Start");
            service.syncProducts();

        } finally {
            log.info("ProductScheduler.checkOutOfSyncProducts - End - took [{}ms]", (System.currentTimeMillis() - start));
        }
    }
}