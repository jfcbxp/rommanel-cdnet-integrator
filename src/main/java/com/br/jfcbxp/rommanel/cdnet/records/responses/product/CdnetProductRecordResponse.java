package com.br.jfcbxp.rommanel.cdnet.records.responses.product;

import java.math.BigDecimal;
import java.util.List;

public record CdnetProductRecordResponse(
        String produtoId,
        String codigoProduto,
        String titulo,
        String codigoCor,
        String codigoFamilia,
        String codigoGrupo,
        String descricao,
        BigDecimal precoSugerido,
        BigDecimal precoEtiqueta,
        String codigoCampanha,
        String codigoTema,
        String codigoEstilo,
        String codigoFormato,
        String ncm,
        boolean inativo,
        List<CdnetSubProductRecordResponse> subProdutos
) {


}