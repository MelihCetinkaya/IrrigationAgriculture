package com.springexample.irrigationagriculture.dto;

import com.springexample.irrigationagriculture.dto.abstractDto.PersonDto;
import com.springexample.irrigationagriculture.entity.User;

import java.util.ArrayList;
import java.util.List;

public class AdminDto extends PersonDto {

    private List<User> users = new ArrayList<>();


}
