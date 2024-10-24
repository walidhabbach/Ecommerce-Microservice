package org.product_service.service.interfaces;
import java.util.List;

import org.product_service.dto.CategoryDto;

public interface CategoryService {

    List<CategoryDto> findAll();
    CategoryDto findById(final Integer  categoryId);
    CategoryDto save(final CategoryDto categoryDto);
    CategoryDto update(final CategoryDto categoryDto);
    CategoryDto update(final Integer  categoryId, final CategoryDto categoryDto);
    void deleteById(final Integer  categoryId);

}