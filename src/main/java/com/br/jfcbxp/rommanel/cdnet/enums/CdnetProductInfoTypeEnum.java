package com.br.jfcbxp.rommanel.cdnet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CdnetProductInfoTypeEnum {

    FORMAT("formato", "/incluirFormato"),
    ACCESSORY("acessorio", "/incluirAcessorio"),
    STYLE("estilo", "/incluirEstilo"),
    THEME("tema", "/incluirTema"),
    COLOR("cores", "/incluirCor"),
    FAMILY("familias", "/incluirFamilia"),
    CAMPAIGN("campanhas", "/incluirCampanha"),
    GROUP("grupos", "/incluirGrupo");
    private final String type;
    private final String endpoint;

}
