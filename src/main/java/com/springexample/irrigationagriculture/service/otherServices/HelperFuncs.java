package com.springexample.irrigationagriculture.service.otherServices;

import com.fazecast.jSerialComm.SerialPort;
import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class HelperFuncs {

    private final PersonRepo personRepo;
    private final JwtService jwtService;
    private final SerialPort  serialPort;

    public HelperFuncs(PersonRepo personRepo, JwtService jwtService, SerialPort serialPort) {
        this.personRepo = personRepo;
        this.jwtService = jwtService;
        this.serialPort = serialPort;
    }


    public Boolean checkUser(String token){

        Person person = (Person) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if(person.getPlantHouse()==null){
            System.out.println("you have not added it to any system");
            return false;
        } else if (!HelperFuncs.checkTimeZone(person) || person.getCommand()==false) {
            System.out.println("you don't have command or timezone");
            return false;
        }
        return true;
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
        if(data.charAt(0)=='t'){
            System.out.println("for temperature");

        } else if (data.length()==1) {
            data = "0"+data;
            byte[] buffer = new byte[] { (byte)(data.charAt(0) - '0'), (byte)(data.charAt(1) - '0') };
            serialPort.writeBytes(buffer, buffer.length);
            System.out.println("Gönderildi: " + data);
        } else{

            byte[] buffer = new byte[] { (byte)(data.charAt(0) - '0'), (byte)(data.charAt(1) - '0') };
            serialPort.writeBytes(buffer, buffer.length);
            System.out.println("Gönderildi: " + data);}
    }



}
