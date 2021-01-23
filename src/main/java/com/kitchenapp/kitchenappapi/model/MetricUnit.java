package com.kitchenapp.kitchenappapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MetricUnit {
    GRAMS("g"),
    MILLILITRES("ml");

    public final String abbreviation;

}
