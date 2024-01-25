package com.br.jfcbxp.rommanel.cdnet.repositorys;

import com.br.jfcbxp.rommanel.cdnet.domains.ProductInventory;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, String> {
    Page<ProductInventory> findAll(Specification<ProductInventory> criteria, Pageable sort);

    @Transactional
    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true
    )
    @Query(
            value = "UPDATE SB2010 SET B2_ESTVTEX = :estoqueVtex where B2_COD = :codigo and B2_LOCAL = :armazem and B2_FILIAL = :empresa",
            nativeQuery = true
    )
    void updateIntegration(
            @Param("produto")
            String codigo,
            @Param("armazem")
            String armazem,
            @Param("empresa")
            String empresa,
            @Param("estoqueVtex")
            BigDecimal estoqueVtex
    );

}

