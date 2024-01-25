package com.br.jfcbxp.rommanel.cdnet.specifications;


import com.br.jfcbxp.rommanel.cdnet.domains.ProductInventory;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductInventorySpecification {

    private static final String ESTOQUE_VTEX = "estoqueVtex";
    private static final String ESTOQUE = "estoque";
    private static final String ARMAZEM = "armazem";
    private static final String EMPRESA = "empresa";


    public static Specification<ProductInventory> findByCriteria(String armazem, String empresa, boolean onlyOutOfSync) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(ARMAZEM), armazem));
            predicates.add(cb.equal(root.get(EMPRESA), empresa));
            
            if (onlyOutOfSync)
                predicates.add(cb.notEqual(root.get(ESTOQUE_VTEX), root.get(ESTOQUE)));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
