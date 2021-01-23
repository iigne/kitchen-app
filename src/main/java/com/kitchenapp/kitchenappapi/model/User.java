package com.kitchenapp.kitchenappapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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
    private List<UserIngredient> userIngredients;

}
