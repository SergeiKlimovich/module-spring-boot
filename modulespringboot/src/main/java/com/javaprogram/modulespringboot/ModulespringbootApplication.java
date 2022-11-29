package com.javaprogram.modulespringboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ModulespringbootApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ModulespringbootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello, world!");
    }
}
