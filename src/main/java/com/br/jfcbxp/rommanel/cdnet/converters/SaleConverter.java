package com.br.jfcbxp.rommanel.cdnet.converters;


import com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams;
import com.br.jfcbxp.rommanel.cdnet.domains.Sale;
import com.br.jfcbxp.rommanel.cdnet.domains.SaleProduct;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetSaleTotalTypeEnum;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetSaleTransactionEnum;
import com.br.jfcbxp.rommanel.cdnet.enums.CdnetSaleTypeEnum;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetSalePaymentRequest;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetSaleProductRequest;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetSaleRequest;
import com.br.jfcbxp.rommanel.cdnet.records.requests.CdNetSaleTotalRequest;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Component
public class SaleConverter extends AbstractConverter<Sale, CdNetSaleRequest> {
    @Value("${cdnet.mario-covas.identification}")
    private String identification;

    @Override
    protected CdNetSaleRequest convert(Sale sale) {
        Function<SaleProduct, BigDecimal> totalMapper = SaleProduct::getProductTotal;

        var total = new CdNetSaleTotalRequest(CdnetSaleTotalTypeEnum.ITEM, sale.getProducts().stream()
                .map(totalMapper)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        var payment = new CdNetSalePaymentRequest(sale.getPaymentDescription(), total.value());
        
        return new CdNetSaleRequest(identification,
                sale.getDocument().concat(sale.getDocumentVersion()),
                sale.getDocumentDate().format(DateTimeFormatter.ofPattern(CdnetInternalParams.INTEGRATION_DATE_TIME_FORMAT)),
                CdnetSaleTransactionEnum.SALE,
                sale.getDocument(),
                sale.getDocumentKey(),
                sale.getProducts().stream().map(product -> new CdNetSaleProductRequest(product.getProductId(), product.getProductQuantity().toBigInteger(), product.getProductTotal())).toList(),
                List.of(payment),
                List.of(total),
                CdnetSaleTypeEnum.WHOLESALE,
                sale.getCustomerDocument()

        );

    }
}