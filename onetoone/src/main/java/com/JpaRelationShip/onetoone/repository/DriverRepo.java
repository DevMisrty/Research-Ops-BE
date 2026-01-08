package com.JpaRelationShip.onetoone.repository;

import com.JpaRelationShip.onetoone.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepo extends JpaRepository<Driver,Long> {
}
