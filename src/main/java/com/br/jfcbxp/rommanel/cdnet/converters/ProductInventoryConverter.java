package com.br.jfcbxp.rommanel.cdnet.converters;


import com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams;
import com.br.jfcbxp.rommanel.cdnet.domains.ProductInventory;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetInventoryRequest;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ProductInventoryConverter extends AbstractConverter<ProductInventory, CdNetInventoryRequest> {

    @Value("${cdnet.inventory.ecommerce-company-code}")
    private String companyCode;

    @Override
    protected CdNetInventoryRequest convert(ProductInventory productInventory) {

        var date = ZonedDateTime.now(ZoneId.of(CdnetInternalParams.ZONE_ID));
        var integrationDate = date.format(DateTimeFormatter.ofPattern(CdnetInternalParams.INTEGRATION_DATE_TIME_FORMAT));

        return new CdNetInventoryRequest(new BigInteger(productInventory.getProductCode().trim()),
                productInventory.getCompanyId(), productInventory.getStock().toBigInteger(),
                integrationDate, 0, productInventory.getCompanyCode().equals(companyCode));

    }
}