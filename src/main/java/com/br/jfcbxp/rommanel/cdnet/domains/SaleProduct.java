package com.br.jfcbxp.rommanel.cdnet.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "INTEGRACAO_CDNET_SALES_ITEMS")
public class SaleProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "REC")
    private Integer id;

    @Column(name = "EMPRESA")
    private String companyCode;

    @Column(name = "DOCUMENTO")
    private String document;

    @Column(name = "SERIE")
    private String documentVersion;

    @Column(name = "CODIGO_PRODUTO")
    private String productId;

    @Column(name = "QUANTIDADE")
    private BigDecimal productQuantity;

    @Column(name = "PRECO_VENDA")
    private BigDecimal productPrice;

    @Column(name = "TOTAL")
    private BigDecimal productTotal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(
            name = "EMPRESA",
            referencedColumnName = "EMPRESA",
            insertable = false,
            updatable = false
    )
    @JoinColumn(
            name = "DOCUMENTO",
            referencedColumnName = "DOCUMENTO",
            insertable = false,
            updatable = false
    )
    @JoinColumn(
            name = "SERIE",
            referencedColumnName = "SERIE",
            insertable = false,
            updatable = false
    )
    private Sale sale;

}
