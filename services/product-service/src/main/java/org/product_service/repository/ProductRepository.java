package org.product_service.repository;

import org.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>{

    List<Product> findAllByIdInOrderById(List<Integer> ids);

    @Query ("SELECT COALESCE(SUM(p.price), 0) FROM Product p WHERE p.id IN :ids")
    BigDecimal calculateTotalPriceByIds(@Param ("ids") List<Integer> ids);
}