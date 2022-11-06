package com.nvidia.display;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value="com.nvidia")
public class DisplayApplication {

    public static void main(String[] args) {
        System.out.println("Start!");
        SpringApplication.run(DisplayApplication.class, args);
    }

}
