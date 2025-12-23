package com.springexample.irrigationagriculture.service;

import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.entity.PlantHouse;
import com.springexample.irrigationagriculture.entity.User;
import com.springexample.irrigationagriculture.entity.enums.TimeZone;
import com.springexample.irrigationagriculture.exception.exceptions.NoUserFoundException;
import com.springexample.irrigationagriculture.repository.*;
import com.springexample.irrigationagriculture.service.otherServices.JwtService;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class AdminService {

    private final JwtService jwtService;
    private final PersonRepo personRepo;
    private final AdminRepo adminRepo;
    private final UserRepo userRepo;
    private final PlantHouseRepo plantHouseRepo;

    public AdminService(JwtService jwtService, PersonRepo personRepo, AdminRepo adminRepo, UserRepo userRepo, PlantHouseRepo plantHouseRepo) {
        this.jwtService = jwtService;
        this.personRepo = personRepo;
        this.adminRepo = adminRepo;
        this.userRepo = userRepo;
        this.plantHouseRepo = plantHouseRepo;

    }

    public String changeCommand(String token,String username,Boolean value) throws NoUserFoundException {

        Admin admin = (Admin) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();
        User user = userRepo.findByUsername(username).orElseThrow(NoUserFoundException::new);

        if(!admin.getUsers().contains(user)){
            System.out.println("There is no such user added to your system.");
            return "There is no such user added to your system.";
        }

        user.setCommand(value);
        userRepo.save(user);
        return "The user's authorization has been changed";

    }

    public String assignScheduler(String token, String username ,String time,String time2)
            throws  NoUserFoundException {

        Admin admin = (Admin) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();
        User user = userRepo.findByUsername(username).orElseThrow(NoUserFoundException::new);

        Set<String> validTimes = Set.of("0", "4", "8", "12", "16", "20", "free");

        if (!validTimes.contains(time) || !validTimes.contains(time2)) {
            return "You can only enter one of the following values: 0, 4, 8, 12, 16, 20, free";
        }

        TimeZone timeZone3 = TimeZone.valueOf("T" + time);
        TimeZone timeZone4 = TimeZone.valueOf("T" + time2);


        if(!admin.getUsers().contains(user)){
            System.out.println("There is no such user added to your system.");
            return "There is no such user added to your system." ;
        }

        user.setTimeZone(timeZone3);
        user.setTimeZone2(timeZone4);
        userRepo.save(user);

        return "user's time intervals have been changed.";

    }

    public String addUser(String token,String username) throws NoUserFoundException {

        Admin admin = (Admin) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();
        User user = userRepo.findByUsername(username).orElseThrow(NoUserFoundException::new);

        if(admin.getUsers().stream().noneMatch(u -> u.getUsername().equalsIgnoreCase(username))){
            admin.getUsers().add(user);
            PlantHouse pt;
            pt=admin.getPlantHouse();
            user.setAdmin(admin);
            user.setPlantHouse(admin.getPlantHouse());
            pt.getPerson().add(user);

            plantHouseRepo.save(pt);
            userRepo.save(user);
            adminRepo.save(admin);

            return "user is added to your system";
        }else{
        System.out.println("already added before");}
        return "already added before";

    }


    public Boolean enterAdminRoom() {

        return true;

    }
}
