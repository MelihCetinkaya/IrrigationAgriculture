package com.springexample.irrigationagriculture.service;

import com.fazecast.jSerialComm.SerialPort;
import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import com.springexample.irrigationagriculture.exception.GeneralException;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class CommonFuncService {

    private static PersonRepo personRepo = null;
    private final JwtService jwtService;
    private final SerialPort serialPort;
    public CommonFuncService(PersonRepo personRepo, JwtService jwtService, SerialPort serialPort) {
        CommonFuncService.personRepo = personRepo;
        this.jwtService = jwtService;
        this.serialPort = serialPort;
    }


    public String changeTempStatus(String token,Boolean status)  {

        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if(!CommonFuncService.checkTimeZone(person) || person.getCommand()==false){
            System.out.println("you don't have command or timezone");
            return "you don't have command or timezone";
        }
        person.getPlantHouse().getAmounts().setTempIsActive(status);
        personRepo.save(person);
        sendData("t"+status);
        return "successfully changed temp status";

    }

    public void changeHumStatus(String username) throws GeneralException {


        //CommonFuncService.checkTimeZone(person);

        //person.getPlantHouse().getValues().setHumIsActive(!person.getPlantHouse().getValues().getHumIsActive());

    }


    public static void changeWthStatus(String username) {


        //CommonFuncService.checkTimeZone(person);

        //person.getPlantHouse().getValues().setWthIsActive(!person.getPlantHouse().getValues().getWthIsActive());

    }


    public static Boolean checkTimeZone(Person person) {

        int hour = LocalDateTime.now(ZoneId.of("Europe/Istanbul")).getHour();

        int t1hour = person.getTimeZone().getValue();
        int t2hour = person.getTimeZone2().getValue();
        boolean access=false;

        while(t1hour!=t2hour){

            if (t1hour == hour) {
                access = true;
                break;
            }

            if(t1hour==23){
                t1hour=0;
            }
            else{
            t1hour++;}

        }

        if(t1hour==1000){
            System.out.println("No time has been assigned to you by the admin.");
            return false;
        } else if (t1hour==0&&t2hour==0) {
            return true;
        } else if (access) {
            return true;
        }
        else{
            System.out.println("You cannot water during these time intervals. Another user is authorized during these time intervals.");
            return false;
        }

    }

    public void sendData(String data) {
        byte[] buffer = data.getBytes();
        serialPort.writeBytes(buffer, buffer.length);
        System.out.println("Gönderildi: " + data);
    }

    public String enableIrrigation(String token,String time){

        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if(!CommonFuncService.checkTimeZone(person) || person.getCommand()==false){
            System.out.println("you don't have command or timezone");
            return "you don't have command or timezone";
        }
        sendData(time);
        System.out.println(time+"seconds of irrigation will be done");
        return time +" seconds of irrigation will be done";


    }
}
