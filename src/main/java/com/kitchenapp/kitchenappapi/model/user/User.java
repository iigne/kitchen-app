package com.kitchenapp.kitchenappapi.model.user;

import com.kitchenapp.kitchenappapi.model.recipe.Recipe;
import com.kitchenapp.kitchenappapi.model.useringredient.ShoppingUserIngredient;
import com.kitchenapp.kitchenappapi.model.useringredient.UserIngredient;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "[user]")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String username;

    private String email;

    private String password;

    @OneToMany(mappedBy = "user")
    private Set<UserIngredient> userIngredients;

    @ManyToMany(mappedBy = "users")
    private Set<Recipe> userRecipes;

    @OneToMany(mappedBy = "user")
    private Set<ShoppingUserIngredient> shoppingUserIngredients;

}
