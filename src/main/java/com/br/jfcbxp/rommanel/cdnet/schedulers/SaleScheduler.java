package com.br.jfcbxp.rommanel.cdnet.schedulers;

import com.br.jfcbxp.rommanel.cdnet.services.CdnetSaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaleScheduler {

    private final CdnetSaleService service;

    @Scheduled(
            cron = "${update-sale.scheduler.cron-value}",
            zone = "America/Sao_Paulo"
    )
    @SchedulerLock(
            name = "SALE",
            lockAtLeastFor = "${update-sale.scheduler.least-lock-time}",
            lockAtMostFor = "${update-sale.scheduler.most-lock-time}"
    )
    public void checkSales() {
        var start = System.currentTimeMillis();
        try {
            log.info("CdnetSaleService.checkSales - Start");
            service.sendSales();

        } finally {
            log.info("CdnetSaleService.checkSales - End - took [{}ms]", (System.currentTimeMillis() - start));
        }
    }
}