package com.example.springauthentication.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @GetMapping("/greeting")
    public String greeting(){
        return "hello";

    }

    @PostMapping("/greeting")
    public String greeting(@RequestBody String name ){
        return "hello " + name;

    }
}
