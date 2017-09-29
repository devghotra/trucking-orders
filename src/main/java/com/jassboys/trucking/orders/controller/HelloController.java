package com.jassboys.trucking.orders.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/api/welcome")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}