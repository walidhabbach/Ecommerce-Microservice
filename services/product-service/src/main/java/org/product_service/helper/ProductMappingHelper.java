package org.product_service.helper;

import org.product_service.dto.CategoryDto;
import org.product_service.dto.OrderLineDto;
import org.product_service.dto.ProductDto;
import org.product_service.model.Product;
import org.product_service.model.Category;
public interface ProductMappingHelper {
	
	public static ProductDto map(final Product product) {
		return ProductDto.builder()
				.id(product.getId())
				.price(product.getPrice())
				.quantity(product.getQuantity())
				.description(product.getDescription())
				.name(product.getName())
				.category(CategoryMappingHelper.map(product.getCategory()))
				.build();
	}
	
	public static Product map(final ProductDto productDto) {
		return Product.builder()
				.id(productDto.getId())
				.price(productDto.getPrice())
				.quantity(productDto.getQuantity())
				.description(productDto.getDescription())
				.name(productDto.getName())
				.category(CategoryMappingHelper.map(productDto.getCategory()))
				.build();
	}
	public static OrderLineDto mapToOrderLineDto(Product product, Integer quantity) {
		return  OrderLineDto.builder()
				.productId(product.getId())
				.quantity(quantity).build();

	}
	
	
}










