package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetInventoryClient;
import com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams;
import com.br.jfcbxp.rommanel.cdnet.domains.ProductInventory;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetInventoryRequest;
import com.br.jfcbxp.rommanel.cdnet.repositorys.ProductInventoryRepository;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetInventoryService;
import com.br.jfcbxp.rommanel.cdnet.specifications.ProductInventorySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.util.NamedFeature;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetInventoryServiceImpl implements CdnetInventoryService {

    private final CdnetInventoryClient client;
    private final CdnetAuthService authService;
    private final ProductInventoryRepository repository;
    private final ModelMapper mapper;
    private final FeatureManager featureManager;

    private static final Feature PRODUCT_INVENTORY_ONLY_OUT_OF_SYNC = new NamedFeature("PRODUCT_INVENTORY_ONLY_OUT_OF_SYNC");

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateInventory(String companyCode, String warehouseCode) {
        log.info("CdNetInventoryServiceImpl.updateInventory - Start");

        var sortBy = Sort.by(Sort.Direction.valueOf(CdnetInternalParams.PAGINATE_SORT_DIRECTION_DEFAULT),
                CdnetInternalParams.PAGINATE_SORT_PRODUCT_INVENTORY_PROPERTIES_DEFAULT);

        var page = PageRequest.of(CdnetInternalParams.PAGINATE_PAGE_DEFAULT, CdnetInternalParams.PAGINATE_ROWS_DEFAULT, sortBy);


        var products = repository.findAll(ProductInventorySpecification.findByCriteria(warehouseCode, companyCode, featureManager.isActive(PRODUCT_INVENTORY_ONLY_OUT_OF_SYNC)),
                page);

        if (!products.isEmpty()) {
            var token = authService.getToken();
            products.stream().forEach(productInventory -> sendInventory(productInventory, token));
        }


        log.info("CdNetInventoryServiceImpl.updateInventory - End");

    }

    private void sendInventory(ProductInventory productInventory, String token) {
        var product = mapper.map(productInventory, CdNetInventoryRequest.class);

        var response = client.updateInventory(token, product);

        log.info("CdNetInventoryServiceImpl.updateInventory - sending integration for product {} - data {}",
                product.fullProductId(), product);

        if (response.success() || response.statusCode().equals(CdnetInternalParams.PRODUCT_NOT_FOUND_ERROR_CODE)) {
            repository.updateIntegration(productInventory.getProductCode(), productInventory.getWarehouseCode(),
                    productInventory.getCompanyCode(), productInventory.getStock());
            log.info("CdNetInventoryServiceImpl.updateInventory - successful integration for product {} - code {} - message: {}, data {}",
                    product.fullProductId(), response.statusCode(), response.message(), response.data());
        } else {
            log.error("CdNetInventoryServiceImpl.updateInventory - unsuccessful integration for product {} - code {} - message: {}, data {}",
                    product.fullProductId(), response.statusCode(), response.message(), response.data());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateInventoryList(String warehouseCode) {
        log.info("CdNetInventoryServiceImpl.updateInventoryList - Start");

        try {
            var token = authService.getToken();
            var products = this.findOutOfSyncProducts(warehouseCode);

            if (!products.isEmpty()) {
                var result = client.updateInventoryList(token, products);
                log.info("CdNetInventoryServiceImpl.updateInventoryList - complete {}", result);
            }


        } catch (
                Exception e) {
            log.error("CdNetInventoryServiceImpl.updateInventoryList - Error message: {}",
                    e.getMessage());
        }
        log.info("CdNetInventoryServiceImpl.updateInventoryList - End");
    }

    private List<CdNetInventoryRequest> findOutOfSyncProducts(String warehouseCode) {
        log.info("CdNetInventoryServiceImpl.findOutOfSyncProducts - Start");

        var sortBy = Sort.by(Sort.Direction.valueOf(CdnetInternalParams.PAGINATE_SORT_DIRECTION_DEFAULT),
                CdnetInternalParams.PAGINATE_SORT_PRODUCT_INVENTORY_PROPERTIES_DEFAULT);

        var page = PageRequest.of(CdnetInternalParams.PAGINATE_PAGE_DEFAULT, CdnetInternalParams.PAGINATE_INVENTORY_ROWS_DEFAULT, sortBy);

        var products = repository.findAll(ProductInventorySpecification.findByCriteria(warehouseCode, null, featureManager.isActive(PRODUCT_INVENTORY_ONLY_OUT_OF_SYNC)),
                page);

        return products.stream().map(productInventory -> {

            repository.updateIntegration(productInventory.getProductCode(), productInventory.getWarehouseCode(),
                    productInventory.getCompanyCode(), productInventory.getStock());

            log.info("CdNetInventoryServiceImpl.updateInventory - successful integration for product {} - warehouse {} - company: {}, stock {}",
                    productInventory.getProductCode(), productInventory.getWarehouseCode(), productInventory.getCompanyCode(), productInventory.getStock());

            return mapper.map(productInventory, CdNetInventoryRequest.class);
        }).toList();


    }
}
