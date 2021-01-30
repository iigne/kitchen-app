package com.kitchenapp.kitchenappapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum MetricUnit {
    GRAMS("g"),
    MILLILITRES("ml");

    public final String abbreviation;

    private static final Map<String, MetricUnit> map = new HashMap<>();

    static {
        for (MetricUnit unit : MetricUnit.values()) {
            map.put(unit.getAbbreviation(), unit);
        }
    }

    public static MetricUnit get(String abbreviation) {
        return map.get(abbreviation);
    }

}
