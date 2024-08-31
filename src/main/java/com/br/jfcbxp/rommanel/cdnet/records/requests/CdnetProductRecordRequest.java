package com.br.jfcbxp.rommanel.cdnet.records.requests;

import com.br.jfcbxp.rommanel.cdnet.records.responses.product.CdnetProductRecordResponse;
import com.br.jfcbxp.rommanel.cdnet.records.responses.product.CdnetSubProductRecordResponse;

import java.math.BigDecimal;
import java.util.List;

public record CdnetProductRecordRequest(
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
        List<CdnetSubProductRecordResponse> subProdutos,
        String imagemBase64
) {

    public CdnetProductRecordRequest(CdnetProductRecordResponse product, String photoEncoded) {
        this(product.produtoId(),
                product.codigoProduto(),
                product.titulo(),
                product.codigoCor(),
                product.codigoFamilia(),
                product.codigoGrupo(),
                product.descricao(),
                product.precoSugerido(),
                product.precoEtiqueta(),
                product.codigoCampanha(),
                product.codigoTema(),
                product.codigoEstilo(),
                product.codigoFormato(),
                product.ncm(),
                product.subProdutos(),
                photoEncoded);
    }
}