package com.innovatech.asignaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.innovatech")
@EnableFeignClients
public class AsignacionesApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsignacionesApplication.class, args);
    }
}