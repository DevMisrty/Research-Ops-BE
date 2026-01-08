package com.JpaRelationShips.OneToOne;

import com.JpaRelationShips.OneToOne.model.Car;
import com.JpaRelationShips.OneToOne.model.Driver;
import com.JpaRelationShips.OneToOne.repsitory.CarRepo;
import com.JpaRelationShips.OneToOne.repsitory.DriverRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class Controller {

    private DriverRepo driverRepo;
    private CarRepo carRepo;

    @PostMapping("/driver")
    public Driver addDriver(@RequestBody Driver driver){
        log.info(String.valueOf(driver));
        return driverRepo.save(driver);
    }

    @PostMapping("/car")
    public Car addCar(@RequestBody Car car){
        log.info(String.valueOf(car));
        return carRepo.save(car);
    }

    @PostMapping("/testData")
    public Driver testData(){
        Driver driver = new Driver();
        Car car = new Car();
        car.setName("car1");

        driver.setName("driver1");
        driver.setCar(car);
        return driverRepo.save(driver);
    }

}
