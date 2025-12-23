package com.springexample.irrigationagriculture.service.otherServices;

import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.entity.Valve;
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

    public FlowService(@SuppressWarnings("rawtypes") PersonRepo personRepo, ValveRepo valveRepo) {
        this.personRepo = personRepo;
        this.valveRepo = valveRepo;
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

        Admin admin = (Admin) personRepo.findByUsername("admin1").orElseThrow();

        double min_interval;
        double max_interval;
        double current_interval;
        boolean activateCheck;

        while (true) {
            try {


                min_interval = admin.getPlantHouse().getAmounts().getMinTemp();
                max_interval = admin.getPlantHouse().getAmounts().getMaxTemp();
                current_interval = admin.getPlantHouse().getAmounts().getTempValue();
                activateCheck = admin.getPlantHouse().getAmounts().getTempIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinTempTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxTempTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidTempTime();

                makeComparison(min_interval, max_interval, current_interval, activateCheck);

                min_interval = admin.getPlantHouse().getAmounts().getMinHum();
                max_interval = admin.getPlantHouse().getAmounts().getMaxHum();
                current_interval = admin.getPlantHouse().getAmounts().getHumValue();
                activateCheck = admin.getPlantHouse().getAmounts().getHumIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinHumTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxHumTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidHumTime();

                makeComparison(min_interval, max_interval, current_interval, activateCheck);

                min_interval = admin.getPlantHouse().getAmounts().getMinWth();
                max_interval = admin.getPlantHouse().getAmounts().getMaxWth();
                current_interval = admin.getPlantHouse().getAmounts().getWthValue();
                activateCheck = admin.getPlantHouse().getAmounts().getWthIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinWthTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxWthTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidWthTime();

                makeComparison(min_interval, max_interval, current_interval, activateCheck);

                min_interval = admin.getPlantHouse().getAmounts().getMinSoil();
                max_interval = admin.getPlantHouse().getAmounts().getMaxSoil();
                current_interval = admin.getPlantHouse().getAmounts().getSoilValue();
                activateCheck = admin.getPlantHouse().getAmounts().getSoilIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinSoilTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxSoilTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidSoilTime();

                makeComparison(min_interval, max_interval, current_interval, activateCheck);

                min_interval = admin.getPlantHouse().getAmounts().getMinWater();
                max_interval = admin.getPlantHouse().getAmounts().getMaxWater();
                current_interval = admin.getPlantHouse().getAmounts().getWaterValue();
                activateCheck = admin.getPlantHouse().getAmounts().getWaterIsActive();

                min_time = admin.getPlantHouse().getAmounts().getMinWaterTime();
                max_time = admin.getPlantHouse().getAmounts().getMaxWaterTime();
                mid_time = admin.getPlantHouse().getAmounts().getMidWaterTime();

                makeComparison(min_interval, max_interval, current_interval, activateCheck);

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

    private void makeComparison(double min_interval, double max_interval, double current_interval, boolean activateCheck) {

        if (!activateCheck) {
            return;
        }
        if (current_interval < min_interval) {

            flowtime += min_time;

        } else if (current_interval > max_interval) {

            flowtime += max_time;
        } else {
            flowtime += mid_time;
        }

    }

}
