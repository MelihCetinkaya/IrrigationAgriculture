package com.springexample.irrigationagriculture.service;

import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.entity.enums.IrrigationMode;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import com.springexample.irrigationagriculture.service.otherServices.HelperFuncs;
import org.springframework.stereotype.Service;

@SuppressWarnings("rawtypes")
@Service
public class AutoIrrigationService implements Runnable {

    private final PersonRepo personRepo;
    private final HelperFuncs helperFuncs;

    public AutoIrrigationService(PersonRepo personRepo, HelperFuncs helperFuncs) {
        this.personRepo = personRepo;
        this.helperFuncs = helperFuncs;
    }

    public void makeAutoIrrigation() {

  while(true) {
            try {
                Thread.sleep(20000);
                Admin admin = (Admin) personRepo.findByUsername("admin1").orElseThrow();
                if (admin.getPlantHouse().getIrrigationMode() != IrrigationMode.SelfIrrigationMode) {

                    String time = String.valueOf(admin.getPlantHouse().getValve().getFlowTime());
                    helperFuncs.sendData(time);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {

        makeAutoIrrigation();
    }
}
