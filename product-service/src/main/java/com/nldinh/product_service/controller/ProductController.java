package com.nldinh.product_service.controller;

import com.nldinh.product_service.dto.ProductRequest;
import com.nldinh.product_service.dto.ProductResponse;
import com.nldinh.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductRequest> createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProduct(){
        return productService.getAllProduct();
    }
}
