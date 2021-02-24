package com.kitchenapp.kitchenappapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseRecipeDTO {
    private int id;
    private int authorId;
    private String title;
    private String imageLink;
    private String method;
    private List<ResponseRecipeIngredientDTO> ingredients;
}
