package com.ayman.my_store_api.controllers;


import com.ayman.my_store_api.dtos.ProductDto;
import com.ayman.my_store_api.entities.Product;
import com.ayman.my_store_api.mappers.ProductMapper;
import com.ayman.my_store_api.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController
{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @GetMapping
    public List<ProductDto> findAllProducts (@RequestParam(required = false,name = "categoryId") Byte categoryId)
    {
        List<Product> products;
        if (categoryId!=null)
            products= productRepository.findByCategoryId(categoryId);
        else
            products = productRepository.findAllWithCategory();
        return products.stream().map(productMapper::toDto).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProduct(@PathVariable Long id)
    {
        var product = productRepository.findProductById(id);
        if (null==product)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(productMapper.toDto(product));
    }
}
