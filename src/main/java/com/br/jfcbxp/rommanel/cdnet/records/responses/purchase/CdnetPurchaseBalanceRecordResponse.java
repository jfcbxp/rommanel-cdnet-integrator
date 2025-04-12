package com.br.jfcbxp.rommanel.cdnet.records.responses.purchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CdnetPurchaseBalanceRecordResponse(
        String razaoSocial,
        String cnpj,
        String distribuidorId,
        String codigoProduto,
        String codigoProdutoCompleto,
        String codigoPedido,
        String codigoPedidoWeb,
        String codigoStatus,
        LocalDateTime dataPedido,
        LocalDateTime dataAgendamento,
        Integer saldo,
        BigDecimal valorUnitario,
        String observacao
) {

}