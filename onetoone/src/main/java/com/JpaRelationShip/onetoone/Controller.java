package com.JpaRelationShip.onetoone;

import com.JpaRelationShip.onetoone.model.Car;
import com.JpaRelationShip.onetoone.model.Driver;
import com.JpaRelationShip.onetoone.repository.CarRepo;
import com.JpaRelationShip.onetoone.repository.DriverRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@AllArgsConstructor
public class Controller {

    private DriverRepo driverRepo;
    private CarRepo carRepo;

    @PostMapping("/driver")
    public ResponseEntity<Driver> addDriver(@RequestBody Driver driver){
        log.info(String.valueOf(driver));
        return new ResponseEntity<>(driverRepo.save(driver), HttpStatus.CREATED);
    }

    @PostMapping("/car")
    public ResponseEntity<Car> addCar(@RequestBody Car car){
        log.info(String.valueOf(car));
        return new ResponseEntity<>(carRepo.save(car),HttpStatus.CREATED);
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<Driver>> getAllDrivers(){
        return new ResponseEntity<>(driverRepo.findAll(),HttpStatus.OK);
    }

    @GetMapping("/Cars")
    public ResponseEntity<List<Car>> getAllCars(){
        return new ResponseEntity<>(carRepo.findAll(),HttpStatus.OK);
    }

    @PostMapping("/testdata")
    public Driver saveTestData(){
        Driver driver = new Driver();
        Car car = new Car();
        driver.setId(100l);
        driver.setName("driver");
        car.setId(1000l);
        car.setName("car100");
        driver.setCar(car);
        return driverRepo.save(driver);
    }
}
