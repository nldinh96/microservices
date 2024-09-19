package com.nldinh.product_service.service;

import com.nldinh.product_service.dto.ProductRequest;
import com.nldinh.product_service.dto.ProductResponse;
import com.nldinh.product_service.model.Product;
import com.nldinh.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
//          This is too basic
//        log.info("Product" + product.getId() + " is saved");

//          More Advanced using placeholder
        log.info("product {} is saved ", product.getId());

    }

    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.parallelStream()
                .map(product -> mapToProductResponse(product))
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
