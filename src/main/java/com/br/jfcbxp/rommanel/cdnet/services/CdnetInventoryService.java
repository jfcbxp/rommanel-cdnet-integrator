package com.br.jfcbxp.rommanel.cdnet.services;

public interface CdnetInventoryService {

    void updateInventoryList(String warehouseCode);

    void updateInventory(String companyCode, String warehouseCode);
}
