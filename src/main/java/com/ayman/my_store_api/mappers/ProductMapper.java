package com.ayman.my_store_api.mappers;

import com.ayman.my_store_api.dtos.ProductDto;
import com.ayman.my_store_api.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper
{
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);
}
