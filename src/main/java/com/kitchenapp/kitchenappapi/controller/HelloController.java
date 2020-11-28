package com.kitchenapp.kitchenappapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/secured-hello")
    public String hello() {
        return "Super secret Hello from Kitchen App API";
    }
}
