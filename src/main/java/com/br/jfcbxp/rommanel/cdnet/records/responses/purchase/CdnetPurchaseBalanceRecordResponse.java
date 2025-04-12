package com.br.jfcbxp.rommanel.cdnet.records.responses.purchase;

import com.br.jfcbxp.rommanel.cdnet.constants.CdnetInternalParams;
import com.br.jfcbxp.rommanel.cdnet.converters.LocalDateFromDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CdnetPurchaseBalanceRecordResponse(
        String razaoSocial,
        String cnpj,
        String distribuidorId,
        String codigoProduto,
        String codigoProdutoCompleto,
        String codigoPedido,
        String codigoPedidoWeb,
        String codigoStatus,
        @JsonFormat(
                pattern = CdnetInternalParams.DATE_PARAMETER_FORMAT_RESPONSE_PATTERN,
                shape = JsonFormat.Shape.STRING
        )
        @JsonDeserialize(using = LocalDateFromDateTimeDeserializer.class)
        LocalDate dataPedido,
        @JsonFormat(
                pattern = CdnetInternalParams.DATE_PARAMETER_FORMAT_RESPONSE_PATTERN,
                shape = JsonFormat.Shape.STRING
        )
        @JsonDeserialize(using = LocalDateFromDateTimeDeserializer.class)
        LocalDate dataAgendamento,
        Integer saldo,
        BigDecimal valorUnitario,
        String observacao
) {

}