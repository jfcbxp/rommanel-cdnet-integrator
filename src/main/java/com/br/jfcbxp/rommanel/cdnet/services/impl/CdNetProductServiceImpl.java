package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetProductClient;
import com.br.jfcbxp.rommanel.cdnet.clients.ProtheusProductClient;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetProductInfoTypeEnum;
import com.br.jfcbxp.rommanel.cdnet.records.requests.ProtheusRequest;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

import static com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams.PAGINATE_ROWS_DEFAULT;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetProductServiceImpl implements CdnetProductService {

    private final CdnetProductClient client;
    private final ProtheusProductClient protheusClient;
    private final CdnetAuthService authService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void syncProducts() {
        log.info("CdNetProductServiceImpl.syncProducts - Start");
        var token = authService.getToken();
        var syncDate = getSyncDate();

        syncProductInfo(token, syncDate);
        syncProduct(token, syncDate);

        log.info("CdNetProductServiceImpl.syncProducts - End");

    }

    private void syncProductInfo(String token, LocalDate syncDate) {
        log.info("CdNetProductServiceImpl.syncProductInfo - Start");

        for (CdnetProductInfoTypeEnum productInfo : CdnetProductInfoTypeEnum.values()) {
            var response = client.getProductsInfo(token, productInfo.getType(), syncDate).data();

            for (Object info : response) {
                log.info("CdNetProductServiceImpl.syncProduct - sendProductInfo {} {} ", productInfo.getEndpoint(), info);

                protheusClient.sendProductInfo(new ProtheusRequest(productInfo.getEndpoint(), info));
            }


        }

        log.info("CdNetProductServiceImpl.syncProductInfo - End");
    }

    private void syncProduct(String token, LocalDate syncDate) {
        log.info("CdNetProductServiceImpl.syncProduct - Start");

        var response = client.getProducts(token, PAGINATE_ROWS_DEFAULT, 1, syncDate).data();

        var totalPages = this.getTotalPages(response.totalItems(), response.quantidadeItensPorPagina());

        for (int page = 1; page <= totalPages; page++) {

            var pageResponse = page > 1 ? client.getProducts(token, PAGINATE_ROWS_DEFAULT, page, syncDate).data() : response;

            for (Object product : pageResponse.produtos()) {
                log.info("CdNetProductServiceImpl.syncProduct - sendProduct ");
                protheusClient.sendProductInfo(new ProtheusRequest("/incluirProduto", product));
            }
        }


        log.info("CdNetProductServiceImpl.syncProduct - End");
    }

    private int getTotalPages(int totalResults, int pageSize) {
        return totalResults % pageSize == 0 ? totalResults / pageSize : totalResults / pageSize + 1;
    }

    private LocalDate getSyncDate() {
        var localDate = LocalDate.now();

        DayOfWeek day = DayOfWeek.of(localDate.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY ? localDate.minusDays(30) : localDate.minusDays(1);
    }

}
