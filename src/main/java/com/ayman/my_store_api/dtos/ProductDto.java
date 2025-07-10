package com.ayman.my_store_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto
{
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
