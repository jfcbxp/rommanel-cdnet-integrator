package com.br.jfcbxp.rommanel.cdnet.specifications;


import com.br.jfcbxp.rommanel.cdnet.domains.ProductInventory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductInventorySpecification {

    private static final String ESTOQUE_VTEX = "estoqueVtex";
    private static final String ESTOQUE = "estoque";

    public static Specification<ProductInventory> findByCriteria() {
        return (root, query, cb) -> {

            return cb.notEqual(root.get(ESTOQUE_VTEX), root.get(ESTOQUE));
        };
    }
}
