package com.springexample.irrigationagriculture.service.otherServices;

import com.springexample.irrigationagriculture.entity.Amounts;
import com.springexample.irrigationagriculture.entity.NotificationTools;
import com.springexample.irrigationagriculture.entity.PlantHouse;
import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import com.springexample.irrigationagriculture.entity.enums.IrrigationMode;
import com.springexample.irrigationagriculture.repository.AmountsRepo;
import com.springexample.irrigationagriculture.repository.NotifToolsRepo;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import com.springexample.irrigationagriculture.repository.PlantHouseRepo;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class IrrigationService {

    private static PersonRepo personRepo = null;
    private final JwtService jwtService;
    private final HelperFuncs helperFuncs;
    private final AmountsRepo amountsRepo;
    private final PlantHouseRepo plantHouseRepo;
    private final NotifToolsRepo  notifToolsRepo;

    public IrrigationService(JwtService jwtService, PersonRepo personRepo, HelperFuncs helperFuncs, AmountsRepo amountsRepo, PlantHouseRepo plantHouseRepo, NotifToolsRepo notifToolsRepo) {

        IrrigationService.personRepo = personRepo;
        this.jwtService = jwtService;
        this.helperFuncs = helperFuncs;
        this.amountsRepo = amountsRepo;
        this.plantHouseRepo = plantHouseRepo;
        this.notifToolsRepo = notifToolsRepo;
    }

    public void changeFactorEffect(String token,Boolean activity,String factor) {

       if(!helperFuncs.checkUser(token)){
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

    public void changeThresholds(String token,String factor,String threshold,String minmax) {

        if(!helperFuncs.checkUser(token)){
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();
        double value;
        value = Double.parseDouble(threshold);

        if(Objects.equals(minmax, "min")){

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

        }
        else{
            System.out.println("Invalid");
            return;
        }

        Amounts amounts;
        amounts = person.getPlantHouse().getAmounts();
        amountsRepo.save(amounts);

    }

    public void changeNotificationActivity(String token,String notifType,Boolean activity) {

        if(!helperFuncs.checkUser(token)){
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

            switch (notifType) {
                case "mail" -> person.getPlantHouse().getNotificationTools().setMailActivity(activity);
                case "telegram" -> person.getPlantHouse().getNotificationTools().setTelegramActivity(activity);
                case "websocket" ->person.getPlantHouse().getNotificationTools().setWebSocketActivity(activity);
                default -> {
                    System.out.println("Invalid notifType");
                    return;
                }
            }

        NotificationTools notificationTools;
        notificationTools = person.getPlantHouse().getNotificationTools();
        notifToolsRepo.save(notificationTools);


    }

    public void changeMode(String token,String irrigationMode) {

        if(!helperFuncs.checkUser(token)){
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        switch (irrigationMode) {
            case "auto" -> person.getPlantHouse().setIrrigationMode(IrrigationMode.AutoIrrigationMode);
            case "self" -> person.getPlantHouse().setIrrigationMode(IrrigationMode.SelfIrrigationMode);
            case "hybrid" ->person.getPlantHouse().setIrrigationMode(IrrigationMode.HybridIrrigationMode);
            default -> {
                System.out.println("Invalid mode");
                return;
            }
        }
        PlantHouse plantHouse;
        plantHouse = person.getPlantHouse();
        plantHouseRepo.save(plantHouse);


    }

    public void changeMinMaxTime(String token,String factor,String minmax,int time) {

        if(!helperFuncs.checkUser(token)){
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if(Objects.equals(minmax, "min")){

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

        }
        else if (minmax.equals("mid")) {

            switch (factor) {
                case "temp" ->
                    person.getPlantHouse().getAmounts().setMidTempTime(time);
                case "hum" ->
                    person.getPlantHouse().getAmounts().setMidHumTime(time);
                case "wth" ->
                    person.getPlantHouse().getAmounts().setMidWthTime(time);
                case "waterLevel" ->
                    person.getPlantHouse().getAmounts().setMidWaterTime(time);
                case "soil" ->
                    person.getPlantHouse().getAmounts().setMidSoilTime(time);
                default ->
                    System.out.println("Invalid factor");




            }

        }
        else{
            System.out.println("Invalid");
            return;
        }

        Amounts amounts;
        amounts = person.getPlantHouse().getAmounts();
        amountsRepo.save(amounts);

    }

    public void makeIrrAccToFlowTime(String token){

        if(!helperFuncs.checkUser(token)){
            return;
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();
        int time =person.getPlantHouse().getValve().getFlowTime();
        if(time < 0 || time >20){/******************/
            time = 10;
        }
        String sTime = String.valueOf(time);
        helperFuncs.sendData(sTime);

    }


}