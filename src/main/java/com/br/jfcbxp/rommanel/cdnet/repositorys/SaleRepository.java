package com.br.jfcbxp.rommanel.cdnet.repositorys;

import com.br.jfcbxp.rommanel.cdnet.domains.Sale;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    Page<Sale> findAll(Specification<Sale> criteria, Pageable sort);

    @Transactional
    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true
    )
    @Query(
            value = "UPDATE SF2010 SET F2_ZDTCDNE = :date, F2_ZHRCDNE = :time where R_E_C_N_O_ = :id",
            nativeQuery = true
    )
    void updateIntegration(
            @Param("date")
            String date,
            @Param("time")
            String time,
            @Param("id")
            Integer id
    );

}

