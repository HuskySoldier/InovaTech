package com.innovatech.usuarios.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of(
            "status", "OK",
            "message", "Microservicio de Usuarios respondiendo",
            "thread", Thread.currentThread().toString() // Verás que es un Virtual Thread
        );
    }
}