package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetInventoryClient;
import com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdNetInventoryServiceImpl implements CdnetInventoryService {

    private final CdnetInventoryClient client;
    private final CdnetAuthService authService;
    private final ProductInventoryRepository repository;
    private final ModelMapper mapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateInventory(String empresa, String armazem) {
        log.info("CdNetInventoryServiceImpl.updateInventory - Start");

        var sortBy = Sort.by(Sort.Direction.valueOf(CdnetInternalParams.PAGINATE_SORT_DIRECTION_DEFAULT),
                CdnetInternalParams.PAGINATE_SORT_PROPERTIES_DEFAULT);

        var page = PageRequest.of(CdnetInternalParams.PAGINATE_PAGE_DEFAULT, CdnetInternalParams.PAGINATE_ROWS_DEFAULT, sortBy);

        var token = authService.getToken();

        repository.findAll(ProductInventorySpecification.findByCriteria(armazem, empresa, true),
                page).stream().forEach(productInventory -> {
                    var product = mapper.map(productInventory, CdNetInventoryRequest.class);
                    var response = client.updateInventory(token, product);
                    if (response.success())
                        repository.updateIntegration(productInventory.getCodigo(), productInventory.getArmazem(),
                                productInventory.getEmpresa(), productInventory.getEstoque());
                }

        );

        log.info("CdNetInventoryServiceImpl.updateInventory - End");

    }

    @Override
    public void updateInventoryList(String empresa, String armazem) {
        log.info("CdNetInventoryServiceImpl.updateInventoryList - Start");

        try {
            var token = authService.getToken();
            var products = this.findOutOfSyncProducts(empresa, armazem);

            if (!products.isEmpty())
                client.updateInventoryList(token, products);

        } catch (
                Exception e) {
            log.error("CdNetInventoryServiceImpl.updateInventoryList - Error message: {}",
                    e.getMessage());
        }
        log.info("CdNetInventoryServiceImpl.updateInventoryList - End");
    }

    @Transactional(rollbackOn = Exception.class)
    private List<CdNetInventoryRequest> findOutOfSyncProducts(String empresa, String armazem) {
        log.info("CdNetInventoryServiceImpl.findOutOfSyncProducts - Start");

        var sortBy = Sort.by(Sort.Direction.valueOf(CdnetInternalParams.PAGINATE_SORT_DIRECTION_DEFAULT),
                CdnetInternalParams.PAGINATE_SORT_PROPERTIES_DEFAULT);

        var page = PageRequest.of(CdnetInternalParams.PAGINATE_PAGE_DEFAULT, CdnetInternalParams.PAGINATE_ROWS_DEFAULT, sortBy);
        
        return repository.findAll(ProductInventorySpecification.findByCriteria(empresa, armazem, true),
                page).stream().map(productInventory -> {
            repository.updateIntegration(productInventory.getCodigo(), productInventory.getArmazem(),
                    productInventory.getEmpresa(), productInventory.getEstoque());
            return mapper.map(productInventory, CdNetInventoryRequest.class);
        }).toList();


    }
}
