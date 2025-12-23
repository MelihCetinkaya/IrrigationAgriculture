package com.springexample.irrigationagriculture.configuration;

import com.springexample.irrigationagriculture.entity.*;
import com.springexample.irrigationagriculture.entity.enums.Role;
import com.springexample.irrigationagriculture.entity.enums.TimeZone;
import com.springexample.irrigationagriculture.repository.*;
import com.springexample.irrigationagriculture.service.otherServices.FlowService;
import com.springexample.irrigationagriculture.service.otherServices.MqttService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartConfig {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepo adminRepo;
    private final PlantHouseRepo plantHouseRepo;
    private final ValveRepo valveRepo;
    private final NotifToolsRepo  notifToolsRepo;
    private final AmountsRepo amountsRepo;
    private final MqttService mqttService;
    private final FlowService flowService;

    public StartConfig(PasswordEncoder passwordEncoder, AdminRepo adminRepo, PlantHouseRepo plantHouseRepo, ValveRepo valveRepo, NotifToolsRepo notifToolsRepo, AmountsRepo amountsRepo, MqttService mqttService, FlowService flowService) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepo = adminRepo;
        this.plantHouseRepo = plantHouseRepo;
        this.valveRepo = valveRepo;
        this.notifToolsRepo = notifToolsRepo;
        this.amountsRepo = amountsRepo;
        this.mqttService = mqttService;
        this.flowService = flowService;
    }

    @PostConstruct
    public void start() {
        if (adminRepo.findByUsername("admin1").isEmpty()) {

            Admin admin = new Admin();
            admin.setUsername("admin1");
            admin.setPassword(passwordEncoder.encode("admin1"));
            admin.setRole(Role.ROLE_ADMIN);
            admin.setCommand(true);
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

            NotificationTools notificationTools = new NotificationTools();
            notifToolsRepo.save(notificationTools);

            plantHouse.setNotificationTools(notificationTools);
            plantHouseRepo.save(plantHouse);

        }

    }

    @PostConstruct
    public void startMqttListenerAndFlowService() {

        new Thread(mqttService).start();

        new Thread(() -> {
            try {
                while (!mqttService.isConnected()) {
                    Thread.sleep(2000);
                }
                new Thread(flowService).start();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }



}

