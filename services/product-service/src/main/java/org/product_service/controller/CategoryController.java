package org.product_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.product_service.dto.CategoryDto;
import org.product_service.dto.InventoryDto;
import org.product_service.dto.OrderLineDto;
import org.product_service.dto.ProductDto;
import org.product_service.service.interfaces.CategoryService;
import org.product_service.service.interfaces.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping ("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        log.info("*** CategoryController; fetch all categories *");
        return ResponseEntity.ok(categoryService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) {
        log.info("*** CategoryController; fetch category by id *");
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        log.info("*** CategoryController; create new category *");
        CategoryDto createdCategory = categoryService.save(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) {
        log.info("*** CategoryController; update category with id *");
        CategoryDto updatedCategory = categoryService.update(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        log.info("*** CategoryController; delete category with id *");
        categoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
