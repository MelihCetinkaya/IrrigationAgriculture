package com.springexample.irrigationagriculture.repository;

import com.springexample.irrigationagriculture.entity.Amounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountsRepo extends JpaRepository<Amounts, Long> {
}
