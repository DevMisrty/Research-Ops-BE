package com.JpaRelationShips.OneToOne.repsitory;

import com.JpaRelationShips.OneToOne.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepo extends JpaRepository<Driver,Long> {
}
