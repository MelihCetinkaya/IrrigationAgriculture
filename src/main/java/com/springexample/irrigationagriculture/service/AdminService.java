package com.springexample.irrigationagriculture.service;

import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.entity.PlantHouse;
import com.springexample.irrigationagriculture.entity.User;
import com.springexample.irrigationagriculture.entity.enums.TimeZone;
import com.springexample.irrigationagriculture.exception.GeneralException;
import com.springexample.irrigationagriculture.repository.*;
import org.springframework.stereotype.Service;


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

    public void changeCommand(String token,String username,Boolean value) throws GeneralException {

        Admin admin = (Admin) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        User user =admin.getUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(GeneralException::new);

        user.setCommand(value);
        userRepo.save(user);

    }

    public void assignScheduler(String token, String username ,TimeZone timeZone,TimeZone timeZone2)
            throws GeneralException {

        Admin admin = (Admin) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        User user =admin.getUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(GeneralException::new);

        user.setTimeZone(timeZone);
        user.setTimeZone2(timeZone2);
        userRepo.save(user);

    }

    public void addUser(String token,String username){

        Admin admin = (Admin) personRepo.findByUsername(jwtService.findUsername(token)).orElseThrow();

        if(admin.getUsers().stream().noneMatch(u -> u.getUsername().equalsIgnoreCase(username))){
            admin.getUsers().add(userRepo.findUserByUsername(username));
            User user = (User) personRepo.findByUsername(username).orElseThrow();
            PlantHouse pt;
            pt=admin.getPlantHouse();
            user.setAdmin(admin);
            user.setPlantHouse(admin.getPlantHouse());
            pt.getPerson().add(user);

            plantHouseRepo.save(pt);
            userRepo.save(user);
            adminRepo.save(admin);
        }else{
        System.out.println("already added before");}

    }


    public Boolean enterAdminRoom() {

        return true;

    }
}
