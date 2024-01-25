package com.br.jfcbxp.rommanel.cdnet.services.impl;

import com.br.jfcbxp.rommanel.cdnet.clients.CdnetInventoryClient;
import com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetInventoryRequest;
import com.br.jfcbxp.rommanel.cdnet.repositorys.ProductInventoryRepository;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetAuthService;
import com.br.jfcbxp.rommanel.cdnet.services.CdnetInventoryService;
import com.br.jfcbxp.rommanel.cdnet.specifications.ProductInventorySpecification;
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
    public void updateInventoryList() {
        client.updateInventoryList(authService.getToken(), this.findOutOfSyncProducts());
        
    }

    private List<CdNetInventoryRequest> findOutOfSyncProducts() {
        log.info("CdNetInventoryServiceImpl.findOutOfSyncProducts - Start");

        var sortBy = Sort.by(Sort.Direction.valueOf(CdnetInternalParams.PAGINATE_SORT_DIRECTION_DEFAULT),
                CdnetInternalParams.PAGINATE_SORT_PROPERTIES_DEFAULT);

        var page = PageRequest.of(CdnetInternalParams.PAGINATE_PAGE_DEFAULT, CdnetInternalParams.PAGINATE_ROWS_DEFAULT, sortBy);

        return repository.findAll(ProductInventorySpecification.findByCriteria(),
                page).stream().map(productInventory -> mapper.map(productInventory, CdNetInventoryRequest.class)).toList();


    }
}
