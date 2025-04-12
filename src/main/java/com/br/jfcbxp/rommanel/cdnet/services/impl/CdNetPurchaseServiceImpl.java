package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetPurchaseClient;
import com.br.jfcbxp.rommanel.cdnet.clients.ProtheusPurchaseClient;
import com.br.jfcbxp.rommanel.cdnet.records.requests.ProtheusRequest;
import com.br.jfcbxp.rommanel.cdnet.records.responses.purchase.CdnetPurchaseBalanceRecordResponse;
import com.br.jfcbxp.rommanel.cdnet.repositorys.CompanyRepository;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetPurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams.PAGINATE_ROWS_DEFAULT;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetPurchaseServiceImpl implements CdnetPurchaseService {

    private static final String DEFAULT_INCLUDE_PURCHASE_PROTHEUS_ENDPOINT = "/incluirSaldoCompra";
    private static final String DEFAULT_DELETE_PURCHASE_PROTHEUS_ENDPOINT = "/deletaSaldoCompra";

    private final CdnetPurchaseClient client;
    private final ProtheusPurchaseClient protheusClient;
    private final CdnetAuthService authService;
    private final CompanyRepository companyRepository;

    @Override
    public void syncPurchase() {
        log.info("CdNetPurchaseServiceImpl.syncPurchase - Start");
        var token = authService.getToken();

        protheusClient.sendPurchase(new ProtheusRequest(DEFAULT_DELETE_PURCHASE_PROTHEUS_ENDPOINT, true));

        try {
            companyRepository.findAll().forEach(company -> {
                syncPurchaseBalance(token, company.getIdentification());
            });
        } catch (Exception e) {
            log.error("CdNetPurchaseServiceImpl.syncPurchaseBalance - sendPurchase error ", e);
        }

        log.info("CdNetPurchaseServiceImpl.syncPurchase - End");

    }

    private void syncPurchaseBalance(String token, String identification) {
        log.info("CdNetPurchaseServiceImpl.syncPurchaseBalance - Start");

        var response = client.getAvailablePurchaseBalance(token, identification, 1, PAGINATE_ROWS_DEFAULT).data();

        var totalPages = response.paginacao().totalPaginas();

        for (int page = 1; page <= totalPages; page++) {

            var pageResponse = page > 1 ? client.getAvailablePurchaseBalance(token, identification, page, PAGINATE_ROWS_DEFAULT).data() : response;

            for (CdnetPurchaseBalanceRecordResponse saldo : pageResponse.saldo()) {
                log.info("CdNetPurchaseServiceImpl.syncPurchaseBalance - sendPurchase ");

                try {
                    protheusClient.sendPurchase(new ProtheusRequest(DEFAULT_INCLUDE_PURCHASE_PROTHEUS_ENDPOINT, saldo));
                } catch (Exception e) {
                    log.error("CdNetPurchaseServiceImpl.syncPurchaseBalance - sendPurchase error ", e);

                }

            }
        }


        log.info("CdNetPurchaseServiceImpl.syncPurchaseBalance - End");
    }


}
