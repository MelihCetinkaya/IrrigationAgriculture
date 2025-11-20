package com.springexample.irrigationagriculture.api;

import com.springexample.irrigationagriculture.dto.AdminDto;
import com.springexample.irrigationagriculture.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public void changeTemperature(@RequestBody AdminDto adminDto) {


    }
}
