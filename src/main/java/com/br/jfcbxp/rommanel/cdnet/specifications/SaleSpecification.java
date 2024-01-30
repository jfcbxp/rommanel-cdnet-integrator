package com.br.jfcbxp.rommanel.cdnet.specifications;


import com.br.jfcbxp.rommanel.cdnet.domains.Sale;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaleSpecification {
    private static final String DOCUMENT_DATE = "documentDate";
    private static final String COMPANY_CODE = "companyCode";


    public static Specification<Sale> findByCriteria(LocalDate date, String companyCode) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.greaterThanOrEqualTo(root.get(DOCUMENT_DATE), date));
            predicates.add(cb.equal(root.get(COMPANY_CODE), companyCode));


            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
