package com.kitchenapp.kitchenappapi.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Category {
    @Id
    private int id;

    private String name;
}
