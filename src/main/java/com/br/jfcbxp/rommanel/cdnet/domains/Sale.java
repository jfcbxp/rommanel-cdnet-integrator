package com.br.jfcbxp.rommanel.cdnet.domains;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "INTEGRACAO_CDNET_SALES")
public class Sale implements Serializable {

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

    @Column(name = "CHAVE")
    private String documentKey;

    @Column(name = "DATA_EMISSAO")
    private LocalDateTime documentDate;

    @Column(name = "CGC")
    private String customerDocument;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "sale",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SaleProduct> products;

}
