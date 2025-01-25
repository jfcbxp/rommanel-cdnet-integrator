package com.br.jfcbxp.rommanel.cdnet.specifications;


import com.br.jfcbxp.rommanel.cdnet.domains.ProductInventory;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductInventorySpecification {

    private static final String ONLINE_STOCK = "onlineStock";
    private static final String STOCK = "stock";
    private static final String WAREHOUSE_CODE = "warehouseCode";
    private static final String COMPANY_CODE = "companyCode";


    public static Specification<ProductInventory> findByCriteria(String warehouseCode, String companyCode, boolean onlyOutOfSync) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(WAREHOUSE_CODE), warehouseCode));
            
            if (Objects.nonNull(companyCode))
                predicates.add(cb.equal(root.get(COMPANY_CODE), companyCode));

            if (onlyOutOfSync)
                predicates.add(cb.notEqual(root.get(ONLINE_STOCK), root.get(STOCK)));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
