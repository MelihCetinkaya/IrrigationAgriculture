package com.springexample.irrigationagriculture.api;

import com.springexample.irrigationagriculture.entity.enums.TimeZone;
import com.springexample.irrigationagriculture.exception.GeneralException;
import com.springexample.irrigationagriculture.exception.exceptions.NoUserFoundException;
import com.springexample.irrigationagriculture.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminApi {

    private final AdminService adminService;

    public AdminApi(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/changeCommand")
    public ResponseEntity<String> changeCommand(@RequestHeader("Authorization") String token, @RequestParam String username, @RequestParam Boolean value)
            throws GeneralException, NoUserFoundException {

        return ResponseEntity.ok(adminService.changeCommand(token,username,value));

    }

    @PostMapping("/assignScheduler")
    public ResponseEntity<String> assignScheduler(@RequestHeader("Authorization")
     String token,@RequestParam String username,@RequestParam String time,@RequestParam String time2) throws GeneralException, NoUserFoundException {

        return ResponseEntity.ok(adminService.assignScheduler(token,username,time,time2));

    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestHeader("Authorization") String token,@RequestParam String username) throws GeneralException, NoUserFoundException {

        return ResponseEntity.ok(adminService.addUser(token,username));

    }


    @GetMapping("/enterRoom")
    public Boolean enterAdminRoom(){

       return adminService.enterAdminRoom();

    }


}
