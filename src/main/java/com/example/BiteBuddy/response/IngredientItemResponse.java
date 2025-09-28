package com.example.BiteBuddy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientItemResponse {
    private Long id;
    private String name;
    private boolean inStock;
    private CategoryResponse category;
}