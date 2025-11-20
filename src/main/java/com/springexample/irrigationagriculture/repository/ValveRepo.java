package com.springexample.irrigationagriculture.repository;

import com.springexample.irrigationagriculture.entity.Valve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface ValveRepo extends JpaRepository<Valve, Integer> {
}
