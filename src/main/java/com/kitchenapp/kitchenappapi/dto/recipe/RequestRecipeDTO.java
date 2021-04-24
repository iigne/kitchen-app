package com.kitchenapp.kitchenappapi.dto.recipe;

import com.kitchenapp.kitchenappapi.dto.recipe.IngredientQuantityDTO;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestRecipeDTO {

    private int id;

    @NotBlank
    private String title;

    private String imageLink;

    @NotBlank
    private String method;

    @NotNull
    @NotEmpty
    private List<IngredientQuantityDTO> ingredients;
}
