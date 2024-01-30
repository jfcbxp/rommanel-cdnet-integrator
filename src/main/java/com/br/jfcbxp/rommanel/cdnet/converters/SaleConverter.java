package com.br.jfcbxp.rommanel.cdnet.converters;


import com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams;
import com.br.jfcbxp.rommanel.cdnet.domains.Sale;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetSaleTransactionEnum;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetSaleTypeEnum;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetSaleProductRequest;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetSaleRequest;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class SaleConverter extends AbstractConverter<Sale, CdNetSaleRequest> {
    @Value("${cdnet.mario-covas.identification}")
    private String identification;

    @Override
    protected CdNetSaleRequest convert(Sale sale) {

        return new CdNetSaleRequest(identification,
                sale.getDocument().concat(sale.getDocumentVersion()),
                sale.getDocumentDate().format(DateTimeFormatter.ofPattern(CdnetInternalParams.INTEGRATION_DATE_TIME_FORMAT)),
                CdnetSaleTransactionEnum.SALE,
                sale.getDocument(),
                sale.getDocumentKey(),
                sale.getProducts().stream().map(product -> new CdNetSaleProductRequest(product.getProductId(), product.getProductQuantity().intValue())).toList(),
                CdnetSaleTypeEnum.WHOLESALE

        );

    }
}