package com.springexample.irrigationagriculture.entity;

import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Admin extends Person {

    @OneToMany
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
