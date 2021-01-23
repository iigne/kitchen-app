package com.kitchenapp.kitchenappapi.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "ingredient")
public class Ingredient {

    @Id
    private int id;

    private String name;
}
