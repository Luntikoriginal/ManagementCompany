package org.management_company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class ManagementCompany {
    public static void main(String[] args) {
        SpringApplication.run(ManagementCompany.class, args);
        System.out.println("Server started on http://localhost:8080");
    }
}