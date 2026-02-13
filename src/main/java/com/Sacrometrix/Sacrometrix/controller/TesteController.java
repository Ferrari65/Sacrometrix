package com.Sacrometrix.Sacrometrix.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping ("/health")
    @PreAuthorize("hasAuthority('ROLE_ANALISTA')")
    public String health(){
        return "ok";
    }
}
