package com.br.jfcbxp.rommanel.cdnet.schedulers;

import com.br.jfcbxp.rommanel.cdnet.services.CdnetPurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PurchaseScheduler {

    private final CdnetPurchaseService service;

    @Scheduled(
            cron = "${update-purchase.scheduler.cron-value}",
            zone = "America/Sao_Paulo"
    )
    @SchedulerLock(
            name = "PURCHASE",
            lockAtLeastFor = "${update-purchase.scheduler.least-lock-time}",
            lockAtMostFor = "${update-purchase.scheduler.most-lock-time}"
    )
    public void checkOutOfSyncPurchases() {
        var start = System.currentTimeMillis();
        try {
            log.info("PurchaseScheduler.checkOutOfSyncPurchases - Start");
            service.syncPurchase();

        } finally {
            log.info("PurchaseScheduler.checkOutOfSyncPurchases - End - took [{}ms]", (System.currentTimeMillis() - start));
        }
    }
}