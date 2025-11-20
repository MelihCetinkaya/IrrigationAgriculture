package com.springexample.irrigationagriculture.api;

import com.springexample.irrigationagriculture.entity.enums.TimeZone;
import com.springexample.irrigationagriculture.exception.GeneralException;
import com.springexample.irrigationagriculture.service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminApi {

    private final AdminService adminService;

    public AdminApi(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/changeCommand")
    public void changeCommand(@RequestHeader("Authorization") String token,@RequestParam String username,@RequestParam Boolean value)
    throws GeneralException {

        adminService.changeCommand(token,username,value);

    }

    @PostMapping("/assignScheduler")
    public void assignScheduler(@RequestHeader("Authorization")
     String token,@RequestParam String username,@RequestParam TimeZone timeZone,@RequestParam TimeZone timeZone2) throws GeneralException {

        adminService.assignScheduler(token,username,timeZone,timeZone2);

    }

    @PostMapping("/addUser")
    public void addUser(@RequestHeader("Authorization") String token,@RequestParam String username) throws GeneralException {

        adminService.addUser(token,username);

    }


    @GetMapping("/enterRoom")
    public Boolean enterAdminRoom(){

       return adminService.enterAdminRoom();

    }


}
