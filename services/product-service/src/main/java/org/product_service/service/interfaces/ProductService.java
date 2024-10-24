package org.product_service.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.product_service.dto.InventoryDto;
import org.product_service.dto.OrderLineDto;
import org.product_service.dto.ProductDto;

public interface ProductService {

    List<ProductDto> findAll();
    ProductDto findById(final Integer  productId);
    ProductDto save(final ProductDto productDto);
    ProductDto update(final ProductDto productDto);
    ProductDto update(final Integer  productId, final ProductDto productDto);
    void deleteById(final Integer  productId);
     List<InventoryDto> isInStock(List<Integer> ids);
    BigDecimal purchaseProducts(List<OrderLineDto> orderLineDtoList) ;



}