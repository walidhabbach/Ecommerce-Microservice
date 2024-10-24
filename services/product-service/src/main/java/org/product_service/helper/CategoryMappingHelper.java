package org.product_service.helper;

import java.util.Optional;

import org.product_service.model.Category;
import org.product_service.dto.CategoryDto;

public interface CategoryMappingHelper {
	
	public static CategoryDto map(final Category category) {

		return CategoryDto.builder()
				.id(category.getId())
				.name(category.getName())
				.build();
	}
	
	public static Category map(final CategoryDto categoryDto) {
		return Category.builder()
				.id(categoryDto.getId())
				.name(categoryDto.getName())
				.build();
	}
	
	
	
}










