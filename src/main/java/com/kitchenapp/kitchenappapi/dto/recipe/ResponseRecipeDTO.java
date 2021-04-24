package com.kitchenapp.kitchenappapi.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseRecipeDTO {
    private int id;
    private int authorId;
    private String title;
    private String imageLink;
    private String method;
    private boolean liked;
    private List<ResponseRecipeIngredientDTO> ingredients;
}
