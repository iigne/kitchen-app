package com.kitchenapp.kitchenappapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String imageLink;

    @Lob
    @Column(name = "method", columnDefinition = "text")
    private String method;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL})
    private Set<RecipeIngredient> recipeIngredients;

}
