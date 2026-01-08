package com.JpaRelationShips.OneToOne.repsitory;

import com.JpaRelationShips.OneToOne.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepo extends JpaRepository<Car,Long> {
}
