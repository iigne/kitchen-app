package com.kitchenapp.kitchenappapi.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Ingredient {

    @Id
    private int id;

    private String name;
}
