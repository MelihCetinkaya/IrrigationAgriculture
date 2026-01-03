package com.springexample.irrigationagriculture.service;

import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import com.springexample.irrigationagriculture.entity.enums.IrrigationMode;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import com.springexample.irrigationagriculture.service.otherServices.HelperFuncs;
import com.springexample.irrigationagriculture.service.otherServices.JwtService;
import org.springframework.stereotype.Service;


@Service
public class CommonFuncService {

    private static PersonRepo personRepo = null;
    private final JwtService jwtService;
    private final HelperFuncs helperFuncs;

    public CommonFuncService(PersonRepo personRepo, JwtService jwtService, HelperFuncs helperFuncs) {
        CommonFuncService.personRepo = personRepo;
        this.jwtService = jwtService;

        this.helperFuncs = helperFuncs;
    }


    public String enableIrrigation(String token,String time){

        if(!helperFuncs.checkUser(token)){
            return "you cannot any change";
        }
        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();
        if(person.getPlantHouse().getIrrigationMode()== IrrigationMode.AutoIrrigationMode){
            return "You cannot activate the irrigation feature while automatic mode is enabled";

        }
        if(Integer.parseInt(time)>20){
           time="20";
        }
        helperFuncs.sendData(time);
        System.out.println(time+"seconds of irrigation will be done");
        return time +" seconds of irrigation will be done";


    }



}
