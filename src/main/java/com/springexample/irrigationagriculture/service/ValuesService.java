package com.springexample.irrigationagriculture.service;

import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import com.springexample.irrigationagriculture.entity.enums.IrrigationMode;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import com.springexample.irrigationagriculture.service.otherServices.HelperFuncs;
import com.springexample.irrigationagriculture.service.otherServices.JwtService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@SuppressWarnings("rawtypes")
@Service
public class ValuesService {

    private final HelperFuncs helperFuncs;
    private final PersonRepo personRepo;
    private final JwtService jwtService;

    public ValuesService(HelperFuncs helperFuncs, PersonRepo personRepo, JwtService jwtService) {

        this.helperFuncs = helperFuncs;
        this.personRepo = personRepo;
        this.jwtService = jwtService;
    }

    public Boolean enterValuesRoom() {
        return true;
    }


    public String showValues(String token, String factor) {

        if (!helperFuncs.checkUser(token)) {
            return "-";
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        switch (factor) {
            case "temp" -> {
                return String.valueOf(person.getPlantHouse().getAmounts().getTempValue());
            }
            case "hum" -> {
                return String.valueOf(person.getPlantHouse().getAmounts().getHumValue());
            }
            case "wth" -> {
                return String.valueOf(person.getPlantHouse().getAmounts().getWthValue());
            }
            case "waterLevel" -> {
                return String.valueOf(person.getPlantHouse().getAmounts().getWaterValue());
            }
            case "soil" -> {
                return String.valueOf(person.getPlantHouse().getAmounts().getSoilValue());
            }
            default -> {
                System.out.println("Invalid factor");
                return null;
            }
        }


    }


    public Boolean checkActivity(String token, String factor) {

        if (!helperFuncs.checkUser(token)) {
            System.out.println("invalid user");
            return null;
        }

        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        switch (factor) {
            case "temp" -> {
                return person.getPlantHouse().getAmounts().getTempIsActive();
            }
            case "hum" -> {
                return person.getPlantHouse().getAmounts().getHumIsActive();
            }
            case "wth" -> {
                return person.getPlantHouse().getAmounts().getWthIsActive();
            }
            case "waterLevel" -> {
                return person.getPlantHouse().getAmounts().getWaterIsActive();
            }
            case "soil" -> {
                return person.getPlantHouse().getAmounts().getSoilIsActive();
            }
            default -> {
                System.out.println("Invalid factor");
                return null;
            }
        }


    }

    public String getMinMaxTime(String token, String factor, String minmax) {

        if (!helperFuncs.checkUser(token)) {
            return "-";
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        //noinspection IfCanBeSwitch
        if (minmax.equals("min")) {

            switch (factor) {
                case "temp" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinTempTime());
                }
                case "hum" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinHumTime());
                }
                case "wth" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinWthTime());
                }
                case "waterLevel" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinWaterTime());
                }
                case "soil" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinSoilTime());
                }
                default -> {
                    System.out.println("Invalid factor");
                    return null;
                }
            }
        } else if (minmax.equals("max")) {

            switch (factor) {
                case "temp" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxTempTime());
                }
                case "hum" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxHumTime());
                }
                case "wth" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxWthTime());
                }
                case "waterLevel" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxWaterTime());
                }
                case "soil" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxSoilTime());
                }
                default -> {
                    System.out.println("Invalid factor");
                    return null;
                }


            }
        } else if (minmax.equals("mid")) {

            switch (factor) {
                case "temp" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMidTempTime());
                }
                case "hum" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMidHumTime());
                }
                case "wth" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMidWthTime());
                }
                case "waterLevel" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMidWaterTime());
                }
                case "soil" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMidSoilTime());
                }
                default -> {
                    System.out.println("Invalid factor");
                    return null;
                }


            }
        } else {
            return "invalid;";
        }
    }

    public String getMinMaxThreshold(String token, String factor, String minmax) {

        if (!helperFuncs.checkUser(token)) {
            return "-";
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if (minmax.equals("min")) {

            switch (factor) {
                case "temp" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinTemp());
                }
                case "hum" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinHum());
                }
                case "wth" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinWth());
                }
                case "waterLevel" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinWater());
                }
                case "soil" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMinSoil());
                }
                default -> {
                    System.out.println("Invalid factor");
                    return null;
                }
            }
        } else if (minmax.equals("max")) {

            switch (factor) {
                case "temp" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxTemp());
                }
                case "hum" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxHum());
                }
                case "wth" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxWth());
                }
                case "waterLevel" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxWater());
                }
                case "soil" -> {
                    return String.valueOf(person.getPlantHouse().getAmounts().getMaxSoil());
                }
                default -> {
                    System.out.println("Invalid factor");
                    return null;
                }


            }
        } else {
            return "invalid;";
        }
    }

    public IrrigationMode checkMode(String token) {

        if (!helperFuncs.checkUser(token)) {
            System.out.println("invalid user");
            return null;
        }

        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        return person.getPlantHouse().getIrrigationMode();


    }

    public Boolean checkNotification(String token,String factor, String notifType) {

        if (!helperFuncs.checkUser(token)) {
            System.out.println("invalid user");
            return null;
        }

        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if(Objects.equals(notifType, "mail")){

            switch (factor) {
                case "temp" -> {
                    return person.getPlantHouse().getNotificationTools().isTempMail();
                }
                case "hum" -> {
                    return person.getPlantHouse().getNotificationTools().isHumMail();
                }
                case "soil" -> {
                    return person.getPlantHouse().getNotificationTools().isSoilMail();
                }
                case "wth" -> {
                    return person.getPlantHouse().getNotificationTools().isRainMail();
                }
                case "waterLevel" -> {
                    return person.getPlantHouse().getNotificationTools().isLevelMail();
                }
                default -> {
                    System.out.println("Invalid factor");
                    return null;
                }
            }

        }

        else if(Objects.equals(notifType, "telegram")){

            switch (factor) {
                case "temp" -> {
                    return person.getPlantHouse().getNotificationTools().isTempTelegram();
                }
                case "hum" -> {
                    return person.getPlantHouse().getNotificationTools().isHumTelegram();
                }
                case "soil" -> {
                    return person.getPlantHouse().getNotificationTools().isSoilTelegram();
                }
                case "wth" -> {
                    return person.getPlantHouse().getNotificationTools().isRainTelegram();
                }
                case "waterLevel" -> {
                    return person.getPlantHouse().getNotificationTools().isLevelTelegram();
                }
                default -> {
                    System.out.println("Invalid factor");
                    return null;
                }
            }

        }

        else if(Objects.equals(notifType, "socket")){

            switch (factor) {
                case "temp" -> {
                    return person.getPlantHouse().getNotificationTools().isTempSocket();
                }
                case "hum" -> {
                    return person.getPlantHouse().getNotificationTools().isHumSocket();
                }
                case "soil" -> {
                    return person.getPlantHouse().getNotificationTools().isSoilSocket();
                }
                case "wth" -> {
                    return person.getPlantHouse().getNotificationTools().isRainSocket();
                }
                case "waterLevel" -> {
                    return person.getPlantHouse().getNotificationTools().isLevelSocket();
                }
                default -> {
                    System.out.println("Invalid factor");
                    return null;
                }
            }

        }


        else{
            System.out.println("invalid notifType");
            return null;
        }

    }

    public double getFlowTime(String token){

        if (!helperFuncs.checkUser(token)) {
            System.out.println("invalid user");
            return 0.0;
        }

        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        return person.getPlantHouse().getValve().getFlowTime();

    }

}
