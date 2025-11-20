package com.springexample.irrigationagriculture.repository;

import com.springexample.irrigationagriculture.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends PersonRepo<User>{

    @Query("SELECT u From User u WHERE u.username = :username ")
    User findUserByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);
}
