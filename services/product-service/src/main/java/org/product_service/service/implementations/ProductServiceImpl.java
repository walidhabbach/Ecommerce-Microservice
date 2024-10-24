package org.product_service.service.implementations;
import org.hibernate.query.Order;
import org.product_service.dto.OrderLineDto;
import org.product_service.exception.wrapper.InsufficientQuantityException;
import org.product_service.model.Product;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.product_service.dto.InventoryDto;
import org.product_service.exception.payload.ExceptionMsg;
import org.product_service.exception.wrapper.ProductNotFoundException;
import org.product_service.helper.ProductMappingHelper;
import org.product_service.service.interfaces.ProductService;
import org.springframework.stereotype.Service;

import org.product_service.dto.ProductDto;
import org.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	@Override
	public List<ProductDto> findAll() {
		log.info("*** ProductDto List, service; fetch all products *");
		return this.productRepository.findAll()
				.stream()
				.map(ProductMappingHelper::map)
				.distinct()
				.collect(Collectors.toList());
	}


	@Override
	public List<InventoryDto> isInStock(List<Integer> ids) {
		log.info("Checking Inventory");
		return productRepository.findAllById(ids).stream()
				.map(product ->
						InventoryDto.builder()
								.id(product.getId())
								.isInStock(product.getQuantity() > 0)
								.build()
				).toList();
	}

	@Override
	public ProductDto findById(final Integer  productId) {
		log.info("*** ProductDto, service; fetch product by id *");
		return this.productRepository.findById(productId)
				.map(ProductMappingHelper::map)
				.orElseThrow(() -> new ProductNotFoundException(String.format("Product with id: %d not found", productId)));
	}

	@Override
	public ProductDto save(final ProductDto productDto) {
		log.info("*** ProductDto, service; save product *");
		return ProductMappingHelper.map(this.productRepository
				.save(ProductMappingHelper.map(productDto)));
	}

	@Override
	public ProductDto update(final ProductDto productDto) {
		log.info("*** ProductDto, service; update product *");
		return ProductMappingHelper.map(this.productRepository
				.save(ProductMappingHelper.map(productDto)));
	}

	@Override
	public ProductDto update(final Integer  productId, final ProductDto productDto) {
		log.info("*** ProductDto, service; update product with productId *");
		return ProductMappingHelper.map(this.productRepository
				.save(ProductMappingHelper.map(this.findById(productId))));
	}

	@Override
	public void deleteById(final Integer productId) {
		log.info("*** Void, service; delete product by id *");
		this.productRepository.delete(ProductMappingHelper
				.map(this.findById(productId)));
	}
	@Transactional(rollbackFor = {ProductNotFoundException.class, InsufficientQuantityException.class})
	public BigDecimal purchaseProducts( List<OrderLineDto> orderLineDtoList) {
		var productIds = orderLineDtoList
				.stream()
				.map(OrderLineDto::getProductId)
				.toList();
		List<Product> storedProducts = productRepository.findAllByIdInOrderById(productIds);
		if (productIds.size() != storedProducts.size()) {
			throw new ProductNotFoundException("One or more products not found");
		}
		List<OrderLineDto> sortedProductsDto = orderLineDtoList
				.stream()
				.sorted(Comparator.comparing(OrderLineDto::getProductId))
				.toList();
		List<OrderLineDto> purchasedProducts = new ArrayList<>();

		for (int i = 0; i < storedProducts.size(); i++) {
			Product product = storedProducts.get(i);
			OrderLineDto orderLineDto = sortedProductsDto.get(i);
			System.out.println("orderLineDto.getQuantity()"+orderLineDto.toString());
			System.out.println("product.getQuantity()"+product.getQuantity());
			System.out.println("product()"+product.toString());

			if (product.getQuantity() < orderLineDto.getQuantity()) {
				throw new InsufficientQuantityException("Insufficient stock quantity for product with ID:: " + product.getId());
			}
			Integer newAvailableQuantity = product.getQuantity() - orderLineDto.getQuantity();
			product.setQuantity(newAvailableQuantity);
			productRepository.save(product);
			purchasedProducts.add(ProductMappingHelper.mapToOrderLineDto(product, orderLineDto.getQuantity()));
		}
		return productRepository.calculateTotalPriceByIds(productIds);
	}


}









