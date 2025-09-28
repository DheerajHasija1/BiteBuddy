package com.example.BiteBuddy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private boolean available;
    private CategoryResponse category;
    private List<IngredientItemResponse> ingredients;
    private LocalDateTime createdAt;
    private List<String> images;
    private boolean seasonal;
    private boolean vegetarian;
}