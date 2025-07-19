package com.ayman.my_store_api.products;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Byte categoryId);
    @EntityGraph(attributePaths = "category")
    @Query ("SELECT p FROM Product p")
    List<Product> findAllWithCategory();

    Product findProductById(Long id);
}
