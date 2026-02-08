package com.springexample.irrigationagriculture.service;

import com.springexample.irrigationagriculture.entity.Amounts;
import com.springexample.irrigationagriculture.entity.NotificationTools;
import com.springexample.irrigationagriculture.entity.PlantHouse;
import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import com.springexample.irrigationagriculture.entity.enums.IrrigationMode;
import com.springexample.irrigationagriculture.repository.AmountsRepo;
import com.springexample.irrigationagriculture.repository.NotifToolsRepo;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import com.springexample.irrigationagriculture.repository.PlantHouseRepo;
import com.springexample.irrigationagriculture.service.otherServices.HelperFuncs;
import com.springexample.irrigationagriculture.service.otherServices.JwtService;
import org.springframework.stereotype.Service;

import java.util.Objects;


@SuppressWarnings("rawtypes")
@Service
public class IrrigationService {

    private static PersonRepo personRepo = null;
    private final JwtService jwtService;
    private final HelperFuncs helperFuncs;
    private final AmountsRepo amountsRepo;
    private final PlantHouseRepo plantHouseRepo;
    private final NotifToolsRepo notifToolsRepo;

    public IrrigationService(JwtService jwtService, PersonRepo personRepo, HelperFuncs helperFuncs, AmountsRepo amountsRepo, PlantHouseRepo plantHouseRepo, NotifToolsRepo notifToolsRepo) {

        IrrigationService.personRepo = personRepo;
        this.jwtService = jwtService;
        this.helperFuncs = helperFuncs;
        this.amountsRepo = amountsRepo;
        this.plantHouseRepo = plantHouseRepo;
        this.notifToolsRepo = notifToolsRepo;
    }

    public void changeFactorEffect(String token, Boolean activity, String factor) {

        if (!helperFuncs.checkUser(token)) {
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        switch (factor) {
            case "temp" -> person.getPlantHouse().getAmounts().setTempIsActive(activity);
            case "hum" -> person.getPlantHouse().getAmounts().setHumIsActive(activity);
            case "wth" -> person.getPlantHouse().getAmounts().setWthIsActive(activity);
            case "waterLevel" -> person.getPlantHouse().getAmounts().setWaterIsActive(activity);
            case "soil" -> person.getPlantHouse().getAmounts().setSoilIsActive(activity);
            default -> {
                System.out.println("Invalid factor");
                return;
            }
        }

        Amounts amounts;
        amounts = person.getPlantHouse().getAmounts();
        amountsRepo.save(amounts);

    }

    public void changeThresholds(String token, String factor, String threshold, String minmax) {

        if (!helperFuncs.checkUser(token)) {
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();
        double value;
        value = Double.parseDouble(threshold);

        if (Objects.equals(minmax, "min")) {

            switch (factor) {
                case "temp" -> person.getPlantHouse().getAmounts().setMinTemp(value);
                case "hum" -> person.getPlantHouse().getAmounts().setMinHum(value);
                case "wth" -> person.getPlantHouse().getAmounts().setMinWth(value);
                case "waterLevel" -> person.getPlantHouse().getAmounts().setMinWater(value);
                case "soil" -> person.getPlantHouse().getAmounts().setMinSoil(value);
                default -> {
                    System.out.println("Invalid factor");
                    return;
                }
            }


        } else if (Objects.equals(minmax, "max")) {

            switch (factor) {
                case "temp" -> person.getPlantHouse().getAmounts().setMaxTemp(value);
                case "hum" -> person.getPlantHouse().getAmounts().setMaxHum(value);
                case "wth" -> person.getPlantHouse().getAmounts().setMaxWth(value);
                case "waterLevel" -> person.getPlantHouse().getAmounts().setMaxWater(value);
                case "soil" -> person.getPlantHouse().getAmounts().setMaxSoil(value);
                default -> {
                    System.out.println("Invalid factor");
                    return;
                }
            }

        } else {
            System.out.println("Invalid");
            return;
        }

        Amounts amounts;
        amounts = person.getPlantHouse().getAmounts();
        amountsRepo.save(amounts);

    }

    public void changeNotificationActivity(String token, String factor, String notifType, Boolean activity) {

        if (!helperFuncs.checkUser(token)) {
            System.out.println("invalid user");
        }

        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if (Objects.equals(notifType, "mail")) {

            switch (factor) {
                case "temp" -> person.getPlantHouse().getNotificationTools().setTempMail(activity);
                case "hum" -> person.getPlantHouse().getNotificationTools().setHumMail(activity);
                case "soil" -> person.getPlantHouse().getNotificationTools().setSoilMail(activity);
                case "wth" -> person.getPlantHouse().getNotificationTools().setRainMail(activity);
                case "waterLevel" -> person.getPlantHouse().getNotificationTools().setLevelMail(activity);
                default -> System.out.println("Invalid factor");
            }

        } else if (Objects.equals(notifType, "telegram")) {

            switch (factor) {
                case "temp" -> person.getPlantHouse().getNotificationTools().setTempTelegram(activity);
                case "hum" -> person.getPlantHouse().getNotificationTools().setHumTelegram(activity);
                case "soil" -> person.getPlantHouse().getNotificationTools().setSoilTelegram(activity);
                case "wth" -> person.getPlantHouse().getNotificationTools().setRainTelegram(activity);
                case "waterLevel" -> person.getPlantHouse().getNotificationTools().setLevelTelegram(activity);
                default -> System.out.println("Invalid factor");
            }

        } else if (Objects.equals(notifType, "socket")) {

            switch (factor) {
                case "temp" -> person.getPlantHouse().getNotificationTools().setTempSocket(activity);
                case "hum" -> person.getPlantHouse().getNotificationTools().setHumSocket(activity);
                case "soil" -> person.getPlantHouse().getNotificationTools().setSoilSocket(activity);
                case "wth" -> person.getPlantHouse().getNotificationTools().setRainSocket(activity);
                case "waterLevel" -> person.getPlantHouse().getNotificationTools().setLevelSocket(activity);
                default -> System.out.println("Invalid factor");
            }

        } else {
            System.out.println("invalid notifType");

        }

        NotificationTools notificationTools;
        notificationTools = person.getPlantHouse().getNotificationTools();
        notifToolsRepo.save(notificationTools);

    }


    public void changeMode(String token, String irrigationMode) {

        if (!helperFuncs.checkUser(token)) {
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        switch (irrigationMode) {
            case "auto" -> person.getPlantHouse().setIrrigationMode(IrrigationMode.AutoIrrigationMode);
            case "self" -> person.getPlantHouse().setIrrigationMode(IrrigationMode.SelfIrrigationMode);
            case "hybrid" -> person.getPlantHouse().setIrrigationMode(IrrigationMode.HybridIrrigationMode);
            default -> {
                System.out.println("Invalid mode");
                return;
            }
        }
        PlantHouse plantHouse;
        plantHouse = person.getPlantHouse();
        plantHouseRepo.save(plantHouse);


    }

    public void changeMinMaxTime(String token, String factor, String minmax, int time) {

        if (!helperFuncs.checkUser(token)) {
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if (Objects.equals(minmax, "min")) {

            switch (factor) {
                case "temp" -> person.getPlantHouse().getAmounts().setMinTempTime(time);
                case "hum" -> person.getPlantHouse().getAmounts().setMinHumTime(time);
                case "wth" -> person.getPlantHouse().getAmounts().setMinWthTime(time);
                case "waterLevel" -> person.getPlantHouse().getAmounts().setMinWaterTime(time);
                case "soil" -> person.getPlantHouse().getAmounts().setMinSoilTime(time);
                default -> {
                    System.out.println("Invalid factor");
                    return;
                }
            }


        } else if (Objects.equals(minmax, "max")) {

            switch (factor) {
                case "temp" -> person.getPlantHouse().getAmounts().setMaxTempTime(time);
                case "hum" -> person.getPlantHouse().getAmounts().setMaxHumTime(time);
                case "wth" -> person.getPlantHouse().getAmounts().setMaxWthTime(time);
                case "waterLevel" -> person.getPlantHouse().getAmounts().setMaxWaterTime(time);
                case "soil" -> person.getPlantHouse().getAmounts().setMaxSoilTime(time);
                default -> {
                    System.out.println("Invalid factor");
                    return;
                }
            }

        } else if (minmax.equals("mid")) {

            switch (factor) {
                case "temp" -> person.getPlantHouse().getAmounts().setMidTempTime(time);
                case "hum" -> person.getPlantHouse().getAmounts().setMidHumTime(time);
                case "wth" -> person.getPlantHouse().getAmounts().setMidWthTime(time);
                case "waterLevel" -> person.getPlantHouse().getAmounts().setMidWaterTime(time);
                case "soil" -> person.getPlantHouse().getAmounts().setMidSoilTime(time);
                default -> System.out.println("Invalid factor");


            }

        } else {
            System.out.println("Invalid");
            return;
        }

        Amounts amounts;
        amounts = person.getPlantHouse().getAmounts();
        amountsRepo.save(amounts);

    }


}