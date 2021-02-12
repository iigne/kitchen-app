package com.kitchenapp.kitchenappapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
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

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name="user_recipe",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="recipe_id")}
    )
    private Set<Recipe> userRecipes;

}
