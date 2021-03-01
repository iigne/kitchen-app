package com.kitchenapp.kitchenappapi.dto.request;

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
    private List<RequestRecipeIngredientDTO> ingredients;
}
