package com.kitchenapp.kitchenappapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "[user]")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String username;

    private String email;

    private String password;

}
