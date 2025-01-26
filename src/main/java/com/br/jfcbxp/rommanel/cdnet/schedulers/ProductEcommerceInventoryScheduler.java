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
public class ProductEcommerceInventoryScheduler {

    private final CdnetInventoryService service;

    @Value("${cdnet.inventory.ecommerce-warehouse-code}")
    private String warehouseCode;

    @Value("${cdnet.inventory.ecommerce-company-code}")
    private String companyCode;

    @Scheduled(
            cron = "${update-ecommerce-inventory.scheduler.cron-value}",
            zone = "America/Sao_Paulo"
    )
    @SchedulerLock(
            name = "PRODUCT_ECOMMERCE_INVENTORY",
            lockAtLeastFor = "${update-ecommerce-inventory.scheduler.least-lock-time}",
            lockAtMostFor = "${update-ecommerce-inventory.scheduler.most-lock-time}"
    )
    public void checkOutOfSyncProducts() {
        var start = System.currentTimeMillis();
        try {
            log.info("ProductEcommerceInventoryScheduler.checkOutOfSyncProducts - Start");
            service.updateInventory(companyCode, warehouseCode);

        } finally {
            log.info("ProductEcommerceInventoryScheduler.checkOutOfSyncProducts - End - took [{}ms]", (System.currentTimeMillis() - start));
        }
    }
}