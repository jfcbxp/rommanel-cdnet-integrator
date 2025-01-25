package com.br.jfcbxp.rommanel.cdnet.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "PRODUTOECOMMERCE")
public class ProductInventory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CODIGO_PRODUTO")
    private String productCode;

    @Column(name = "PRECO_PRODUTO")
    private BigDecimal price;

    @Column(name = "ESTOQUE_PRODUTO")
    private BigDecimal stock;

    @Column(name = "EMPRESA")
    private String companyCode;

    @Column(name = "ARMAZEM")
    private String warehouseCode;

    @Column(name = "ESTOQUE_VTEX")
    private BigDecimal onlineStock;

    @Column(name = "CGC_FILIAL")
    private String companyId;


}
