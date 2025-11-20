package com.springexample.irrigationagriculture.repository;

import com.springexample.irrigationagriculture.entity.abstractClasses.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepo <T extends Person> extends JpaRepository<T,Long> {

    Optional<T> findByUsername(String username);



}
