package com.springexample.irrigationagriculture.service.otherServices;

import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.entity.SensorBuffer;
import com.springexample.irrigationagriculture.entity.Valve;
import com.springexample.irrigationagriculture.entity.enums.SensorType;
import com.springexample.irrigationagriculture.repository.NotifToolsRepo;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import com.springexample.irrigationagriculture.repository.ValveRepo;
import org.springframework.stereotype.Service;

@Service
public class FlowService implements Runnable {

    static int flowtime = 0;

    static int min_time;
    static int max_time;
    static int mid_time;

    @SuppressWarnings("rawtypes")
    private final PersonRepo personRepo;
    private final ValveRepo valveRepo;
    private final TelegramService  telegramService;
    private final MailService mailService;
    private final NotifToolsRepo notifToolsRepo;

    public FlowService(@SuppressWarnings("rawtypes") PersonRepo personRepo, ValveRepo valveRepo, TelegramService telegramService, MailService mailService, NotifToolsRepo notifToolsRepo) {
        this.personRepo = personRepo;
        this.valveRepo = valveRepo;
        this.telegramService = telegramService;
        this.mailService = mailService;
        this.notifToolsRepo = notifToolsRepo;
    }

    @Override
    public void run() {

        try {
            calculateNewFlowTime();
        } catch (InterruptedException e) {
            System.out.println("flow service error");
            throw new RuntimeException(e);
        }
    }


    public void calculateNewFlowTime() throws InterruptedException {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        double min_interval;
        double max_interval;
        double current_interval;
        boolean activateCheck;

        while (true) {
            try {
                Admin admin = (Admin) personRepo.findByUsername("admin1").orElseThrow();

                min_interval = admin.getPlantHouse().getAmounts().getMinTemp();
                max_interval = admin.getPlantHouse().getAmounts().getMaxTemp();
                current_interval = admin.getPlantHouse().getAmounts().getTempValue();
                activateCheck = admin.getPlantHouse().getAmounts().getTempIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinTempTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxTempTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidTempTime();

                SensorBuffer.add(SensorType.TEMP,current_interval);

                makeComparison("temperature",min_interval, max_interval, current_interval, activateCheck);

                min_interval = admin.getPlantHouse().getAmounts().getMinHum();
                max_interval = admin.getPlantHouse().getAmounts().getMaxHum();
                current_interval = admin.getPlantHouse().getAmounts().getHumValue();
                activateCheck = admin.getPlantHouse().getAmounts().getHumIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinHumTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxHumTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidHumTime();

                SensorBuffer.add(SensorType.HUM,current_interval);

                makeComparison("humidity",min_interval, max_interval, current_interval, activateCheck);

                min_interval = admin.getPlantHouse().getAmounts().getMinWth();
                max_interval = admin.getPlantHouse().getAmounts().getMaxWth();
                current_interval = admin.getPlantHouse().getAmounts().getWthValue();
                activateCheck = admin.getPlantHouse().getAmounts().getWthIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinWthTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxWthTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidWthTime();

                SensorBuffer.add(SensorType.RAIN,current_interval);

                makeComparison("weather",min_interval, max_interval, current_interval, activateCheck);

                min_interval = admin.getPlantHouse().getAmounts().getMinSoil();
                max_interval = admin.getPlantHouse().getAmounts().getMaxSoil();
                current_interval = admin.getPlantHouse().getAmounts().getSoilValue();
                activateCheck = admin.getPlantHouse().getAmounts().getSoilIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinSoilTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxSoilTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidSoilTime();

                SensorBuffer.add(SensorType.SOIL,current_interval);

                makeComparison("soil moisture",min_interval, max_interval, current_interval, activateCheck);

                min_interval = admin.getPlantHouse().getAmounts().getMinWater();
                max_interval = admin.getPlantHouse().getAmounts().getMaxWater();
                current_interval = admin.getPlantHouse().getAmounts().getWaterValue();
                activateCheck = admin.getPlantHouse().getAmounts().getWaterIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinWaterTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxWaterTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidWaterTime();

                makeComparison("level",min_interval, max_interval, current_interval, activateCheck);

                admin.getPlantHouse().getValve().setFlowTime(flowtime);

                Valve valve;
                valve = admin.getPlantHouse().getValve();
                valveRepo.save(valve);


                flowtime=0;
                //noinspection BusyWait
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("flow service interrupted");
                break;
            } catch (Exception e) {
                System.out.println("flow service error");
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }

        }
    }

    private void makeComparison(String factor,double min_interval, double max_interval, double current_interval, boolean activateCheck) {

        if (!activateCheck) {
            return;
        }

        if (current_interval < min_interval) {

            flowtime += min_time;
            deactivateFactorNotif(factor +" factor dropped below the specified values.",factor);


        } else if (current_interval > max_interval) {

            flowtime += max_time;
            deactivateFactorNotif(factor +" factor exceeded the specified values.",factor);

        } else {
            flowtime += mid_time;
        }

    }

    private void deactivateFactorNotif(String message,String factor){

        Admin admin = (Admin) personRepo.findByUsername("admin1").orElseThrow();

        switch (factor) {
            case "temperature" -> {

                if(admin.getPlantHouse().getNotificationTools().isTempMail()){
                    telegramService.sendMessage(message);
                    admin.getPlantHouse().getNotificationTools().setTempMail(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
                if(admin.getPlantHouse().getNotificationTools().isTempTelegram()){
                    mailService.sendMail(message);
                    admin.getPlantHouse().getNotificationTools().setTempTelegram(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }

            }
            case "humidity" -> {
                if(admin.getPlantHouse().getNotificationTools().isHumMail()){
                    mailService.sendMail(message);
                    admin.getPlantHouse().getNotificationTools().setHumMail(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
                if(admin.getPlantHouse().getNotificationTools().isHumTelegram()){
                    telegramService.sendMessage(message);
                    admin.getPlantHouse().getNotificationTools().setHumTelegram(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
            }
            case "weather" -> {
                if(admin.getPlantHouse().getNotificationTools().isRainMail()){
                    mailService.sendMail(message);
                    admin.getPlantHouse().getNotificationTools().setRainMail(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
                if(admin.getPlantHouse().getNotificationTools().isRainTelegram()){
                    telegramService.sendMessage(message);
                    admin.getPlantHouse().getNotificationTools().setRainTelegram(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
            }
            case "soil moisture" -> {
                if(admin.getPlantHouse().getNotificationTools().isSoilMail()){
                    mailService.sendMail(message);
                    admin.getPlantHouse().getNotificationTools().setSoilMail(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
                if(admin.getPlantHouse().getNotificationTools().isSoilTelegram()){
                    telegramService.sendMessage(message);
                    admin.getPlantHouse().getNotificationTools().setSoilTelegram(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
            }
            case "level" -> {
                if(admin.getPlantHouse().getNotificationTools().isLevelMail()){
                    mailService.sendMail(message);
                    admin.getPlantHouse().getNotificationTools().setLevelMail(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
                if(admin.getPlantHouse().getNotificationTools().isLevelTelegram()){
                    telegramService.sendMessage(message);
                    admin.getPlantHouse().getNotificationTools().setLevelTelegram(false);
                    notifToolsRepo.save(admin.getPlantHouse().getNotificationTools());
                }
            }
            default -> System.out.println("wrong factor");
        }
    }


}
