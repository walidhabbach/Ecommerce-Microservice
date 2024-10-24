package org.product_service.repository;

import org.product_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer > {
	
	
	
}
