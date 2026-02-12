package com.Sacrometrix.Sacrometrix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping ("/health")
    public String health(){
        return "ok";
    }
}
