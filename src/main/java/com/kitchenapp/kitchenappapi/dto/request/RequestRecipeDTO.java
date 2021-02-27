package com.kitchenapp.kitchenappapi.dto.request;

import lombok.*;

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

    @NotNull
    private String title;

    private String imageLink;

    @NotNull
    private String method;

    @NotNull
    @NotEmpty
    private List<RequestRecipeIngredientDTO> ingredients;
}
