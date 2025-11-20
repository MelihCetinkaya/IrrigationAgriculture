package com.springexample.irrigationagriculture;

import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.repository.AdminRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class IrrigationAgricultureApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrrigationAgricultureApplication.class, args);


    }

}
