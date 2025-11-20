package com.springexample.irrigationagriculture.configuration;

import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.entity.Amounts;
import com.springexample.irrigationagriculture.entity.PlantHouse;
import com.springexample.irrigationagriculture.entity.Valve;
import com.springexample.irrigationagriculture.entity.enums.Role;
import com.springexample.irrigationagriculture.entity.enums.TimeZone;
import com.springexample.irrigationagriculture.repository.AdminRepo;
import com.springexample.irrigationagriculture.repository.AmountsRepo;
import com.springexample.irrigationagriculture.repository.PlantHouseRepo;
import com.springexample.irrigationagriculture.repository.ValveRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartConfig {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepo  adminRepo;
    private final PlantHouseRepo plantHouseRepo;
    private final ValveRepo valveRepo;
    private final AmountsRepo amountsRepo;
    public StartConfig(PasswordEncoder passwordEncoder, AdminRepo  adminRepo, PlantHouseRepo plantHouseRepo, ValveRepo valveRepo, AmountsRepo amountsRepo) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepo = adminRepo;
        this.plantHouseRepo = plantHouseRepo;
        this.valveRepo = valveRepo;
        this.amountsRepo = amountsRepo;
    }

    @PostConstruct
    public void start() {
       if(adminRepo.findByUsername("admin1").isEmpty()) {

           Admin admin = new Admin();
           admin.setUsername("admin1");
           admin.setPassword(passwordEncoder.encode("admin1"));
           admin.setRole(Role.ROLE_ADMIN);
           admin.setCommand(true) ;
           admin.setTimeZone(TimeZone.T0);
           admin.setTimeZone2(TimeZone.T0);

           adminRepo.save(admin);

           PlantHouse plantHouse = new PlantHouse();
           plantHouse.getPerson().add(admin);
           plantHouseRepo.save(plantHouse);

           admin.setPlantHouse(plantHouse);
           adminRepo.save(admin);

           Valve valve = new Valve();
           valve.setPlantHouse(plantHouse);
           valveRepo.save(valve);

           plantHouse.setValve(valve);
           plantHouseRepo.save(plantHouse);

           Amounts amounts = new Amounts();
           amounts.setPlantHouse(plantHouse);
           amountsRepo.save(amounts);

           plantHouse.setAmounts(amounts);
           plantHouseRepo.save(plantHouse);


       }

    }



}

