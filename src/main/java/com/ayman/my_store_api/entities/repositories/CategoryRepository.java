package com.ayman.my_store_api.entities.repositories;

import com.ayman.my_store_api.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
