package com.springexample.irrigationagriculture.service;

import com.springexample.irrigationagriculture.repository.UserRepo;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


}
