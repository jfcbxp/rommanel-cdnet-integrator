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
import java.time.LocalDate;

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
    private String codigo;

    @Column(name = "DESCRICAO_PRODUTO")
    private String descricao;

    @Column(name = "PRECO_PRODUTO")
    private BigDecimal preco;

    @Column(name = "ESTOQUE_PRODUTO")
    private BigDecimal estoque;

    @Column(name = "DATA_MOVIMENTO")
    private LocalDate dataMovimento;

    @Column(name = "EMPRESA")
    private String empresa;

    @Column(name = "ARMAZEM")
    private String armazem;

    @Column(name = "ESTOQUE_GERAL_ARMAZEM_10")
    private BigDecimal estoqueGeralArmazem10;

    @Column(name = "ESTOQUE_VTEX")
    private BigDecimal estoqueVtex;


}
