package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetProductClient;
import com.br.jfcbxp.rommanel.cdnet.clients.CdnetProductPhotoClient;
import com.br.jfcbxp.rommanel.cdnet.clients.ProtheusProductClient;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetProductInfoTypeEnum;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdnetProductRecordRequest;
import com.br.jfcbxp.rommanel.cdnet.records.requests.ProtheusRequest;
import com.br.jfcbxp.rommanel.cdnet.records.responses.product.CdnetProductRecordResponse;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Base64;

import static com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams.PAGINATE_ROWS_DEFAULT;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetProductServiceImpl implements CdnetProductService {

    private static final String DEFAULT_PHOTO_FORMAT = ".jpg";
    private static final String DEFAULT_PRODUCT_PROTHEUS_ENDPOINT = "/incluirProduto";
    private final CdnetProductClient client;
    private final CdnetProductPhotoClient photoClient;
    private final ProtheusProductClient protheusClient;
    private final CdnetAuthService authService;

    @Override
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

            for (CdnetProductRecordResponse product : pageResponse.produtos()) {
                log.info("CdNetProductServiceImpl.syncProduct - sendProduct ");

                var photoEncoded = getProductPhotoBase64(product.codigoProduto());
                var productRequest = new CdnetProductRecordRequest(product, photoEncoded);

                protheusClient.sendProductInfo(new ProtheusRequest(DEFAULT_PRODUCT_PROTHEUS_ENDPOINT, productRequest));

            }
        }


        log.info("CdNetProductServiceImpl.syncProduct - End");
    }


    private String getProductPhotoBase64(String productId) {
        log.info("CdNetProductServiceImpl.getProductPhotoBase64 - Start");
        String photoEncoded = "";
        try {
            var response = photoClient.downloadPhoto(productId.concat(DEFAULT_PHOTO_FORMAT));
            if (response.status() == HttpStatus.OK.value()) {
                byte[] bytes = IOUtils.toByteArray(response.body().asInputStream());
                photoEncoded = Base64.getEncoder().encodeToString(bytes);
            }
        } catch (Exception e) {
            log.info("CdNetProductServiceImpl.getProductPhotoBase64 - Error {}", e.getMessage());
        }

        log.info("CdNetProductServiceImpl.getProductPhotoBase64 - End");

        return photoEncoded;

    }

    private int getTotalPages(int totalResults, int pageSize) {
        return totalResults % pageSize == 0 ? totalResults / pageSize : totalResults / pageSize + 1;
    }

    private LocalDate getSyncDate() {
        var localDate = LocalDate.now();

        DayOfWeek day = DayOfWeek.of(localDate.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY ?
                localDate.minusDays(30) : localDate.minusDays(1);
    }

}
