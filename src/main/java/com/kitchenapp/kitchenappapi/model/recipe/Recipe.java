package com.kitchenapp.kitchenappapi.model.recipe;

import com.kitchenapp.kitchenappapi.model.user.User;
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

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<RecipeIngredient> recipeIngredients;

    @ManyToOne
    @JoinColumn(name = "author_user_id")
    private User author;

    @ManyToMany
    @JoinTable(
            name="user_recipe",
            joinColumns = {@JoinColumn(name="recipe_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")}
    )
    private Set<User> users;

}
