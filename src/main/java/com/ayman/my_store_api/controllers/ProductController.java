package com.ayman.my_store_api.controllers;
import com.ayman.my_store_api.dtos.ProductDto;
import com.ayman.my_store_api.entities.Product;
import com.ayman.my_store_api.mappers.ProductMapper;
import com.ayman.my_store_api.repositories.CategoryRepository;
import com.ayman.my_store_api.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController
{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

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

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto , UriComponentsBuilder uriBuilder)
    {
        var category =  categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (null==category)
            return ResponseEntity.badRequest().build();
        var product=productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        var uri=uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct (@PathVariable(name = "id") Long id , @RequestBody ProductDto productDto)
    {
        var category =  categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (null==category)
            return ResponseEntity.badRequest().build();
        var pro = productRepository.findById(id).orElse(null);
        if (null == pro)
           return ResponseEntity.notFound().build();
        productMapper.update(productDto,pro);
        pro.setCategory(category);
        productRepository.save(pro);
        productDto.setId(pro.getId());
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (@PathVariable(name = "id") Long id )
    {
        var pro = productRepository.findById(id).orElse(null);
        if (null == pro)
            return ResponseEntity.notFound().build();
        productRepository.delete(pro);
        return ResponseEntity.noContent().build();
    }
}
