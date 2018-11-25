package com.oocl.cultivation.test;

import com.oocl.cultivation.Car;
import com.oocl.cultivation.ParkingLot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {

    @Test
    void should_return_correct_number_of_available_parking_position_after_parking_car(){
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car();

        parkingLot.parkCar(car);

        assertEquals(9, parkingLot.getAvailableParkingPosition());
    }

    @Test
    void should_return_zero_available_parking_position_when_parking_lot_is_full(){
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car();

        parkingLot.parkCar(car);

        assertEquals(0, parkingLot.getAvailableParkingPosition());
    }

    @Test
    void should_return_correct_available_position_rate_after_parking_car(){
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car();

        parkingLot.parkCar(car);

        assertEquals(0.9, parkingLot.getAvailablePositionRate());
    }

    @Test
    void should_return_zero_available_position_rate_when_parking_lot_is_full(){
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car();

        parkingLot.parkCar(car);

        assertEquals(0, parkingLot.getAvailablePositionRate());
    }
}
