package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetSaleClient;
import com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams;
import com.br.jfcbxp.rommanel.cdnet.domains.Sale;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetSaleRequest;
import com.br.jfcbxp.rommanel.cdnet.repositorys.SaleRepository;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetSaleService;
import com.br.jfcbxp.rommanel.cdnet.specifications.SaleSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetSaleServiceImpl implements CdnetSaleService {

    private static final String NO_VALID_PRODUCTS = "NO_VALID_PRODUCTS";
    private final CdnetSaleClient client;
    private final CdnetAuthService authService;
    private final SaleRepository repository;
    private final ModelMapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void sendSales() {
        log.info("CdNetSaleServiceImpl.sendSales - Start");

        var sortBy = Sort.by(Sort.Direction.valueOf(CdnetInternalParams.PAGINATE_SORT_DIRECTION_DEFAULT),
                CdnetInternalParams.PAGINATE_SORT_SALE_PROPERTIES_DEFAULT);

        var page = PageRequest.of(CdnetInternalParams.PAGINATE_PAGE_DEFAULT, CdnetInternalParams.PAGINATE_ROWS_DEFAULT, sortBy);

        var sales = repository.findAll(SaleSpecification.findByCriteria(LocalDate.ofYearDay(2024, 1)),
                page);

        if (!sales.isEmpty()) {
            var token = authService.getToken();
            sales.stream().forEach(sale ->
                    this.sendSale(sale, token)
            );
        }

        log.info("CdNetSaleServiceImpl.sendSales - End");

    }

    private void sendSale(Sale sale, String token) {
        var date = ZonedDateTime.now(ZoneId.of(CdnetInternalParams.ZONE_ID));
        var integrationTime = date.getHour() + ":" + date.getMinute();
        var integrationDate = date.format(DateTimeFormatter.ofPattern(CdnetInternalParams.INTEGRATION_DATE_FORMAT));

        if (!sale.getProducts().isEmpty()) {
            repository.updateIntegration(integrationDate,
                    integrationTime, NO_VALID_PRODUCTS, sale.getId());
            return;
        }
        
        var saleRequest = mapper.map(sale, CdNetSaleRequest.class);
        var response = client.sendSale(token, List.of(saleRequest));

        if (Strings.isNotEmpty(response)) {
            log.info("CdNetSaleServiceImpl.sendSales - successful integration sale {} {}",
                    response, sale.getDocumentKey());
            repository.updateIntegration(integrationDate,
                    integrationTime, response, sale.getId());
        } else {
            log.error("CdNetSaleServiceImpl.sendSales - unsuccessful integration sale {} {}",
                    response, sale.getDocumentKey());
        }
    }
}
