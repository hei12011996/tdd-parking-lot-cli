package com.oocl.cultivation.test;

import com.oocl.cultivation.Car;
import com.oocl.cultivation.SmartParkingBoy;
import com.oocl.cultivation.ParkingLot;
import com.oocl.cultivation.ParkingTicket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SmartParkingBoyTest {
    @Test
    void should_park_a_car_to_a_parking_lot_and_get_it_back() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = smartParkingBoy.park(car);
        Car fetched = smartParkingBoy.fetch(ticket);

        assertSame(fetched, car);
    }

    @Test
    void should_park_multiple_cars_to_a_parking_lot_and_get_them_back() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();

        ParkingTicket firstTicket = smartParkingBoy.park(firstCar);
        ParkingTicket secondTicket = smartParkingBoy.park(secondCar);

        Car fetchedByFirstTicket = smartParkingBoy.fetch(firstTicket);
        Car fetchedBySecondTicket = smartParkingBoy.fetch(secondTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
    }

    @Test
    void should_not_fetch_any_car_once_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        Car car = new Car();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingTicket ticket = smartParkingBoy.park(car);

        assertNull(smartParkingBoy.fetch(wrongTicket));
        assertSame(car, smartParkingBoy.fetch(ticket));
    }

    @Test
    void should_query_message_once_the_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        smartParkingBoy.fetch(wrongTicket);
        String message = smartParkingBoy.getLastErrorMessage();

        assertEquals("Unrecognized parking ticket.", message);
    }

    @Test
    void should_clear_the_message_once_the_operation_is_succeeded() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        smartParkingBoy.fetch(wrongTicket);
        assertNotNull(smartParkingBoy.getLastErrorMessage());

        ParkingTicket ticket = smartParkingBoy.park(new Car());
        assertNotNull(ticket);
        assertNull(smartParkingBoy.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_any_car_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = smartParkingBoy.park(car);

        assertNull(smartParkingBoy.fetch(null));
        assertSame(car, smartParkingBoy.fetch(ticket));
    }

    @Test
    void should_query_message_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);

        smartParkingBoy.fetch(null);

        assertEquals(
                "Please provide your parking ticket.",
                smartParkingBoy.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_any_car_once_ticket_has_been_used() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = smartParkingBoy.park(car);
        smartParkingBoy.fetch(ticket);

        assertNull(smartParkingBoy.fetch(ticket));
    }

    @Test
    void should_query_error_message_for_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = smartParkingBoy.park(car);
        smartParkingBoy.fetch(ticket);
        smartParkingBoy.fetch(ticket);

        assertEquals(
                "Unrecognized parking ticket.",
                smartParkingBoy.getLastErrorMessage()
        );
    }

    @Test
    void should_not_park_cars_to_parking_lot_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);

        smartParkingBoy.park(new Car());

        assertNull(smartParkingBoy.park(new Car()));
    }

    @Test
    void should_get_message_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);

        smartParkingBoy.park(new Car());
        smartParkingBoy.park(new Car());

        assertEquals("The parking lot is full.", smartParkingBoy.getLastErrorMessage());
    }

    @Test
    void should_park_cars_to_parking_lot_which_contains_more_empty_position_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(firstParkingLot, secondParkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = smartParkingBoy.park(firstCar);
        assertEquals(9, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket secondTicket = smartParkingBoy.park(secondCar);
        assertEquals(8, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = smartParkingBoy.park(thirdCar);
        assertEquals(7, secondParkingLot.getAvailableParkingPosition());

        Car fetchedByFirstTicket = smartParkingBoy.fetch(firstTicket);
        Car fetchedBySecondTicket = smartParkingBoy.fetch(secondTicket);
        Car fetchedByThirdTicket = smartParkingBoy.fetch(thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }

    @Test
    void should_not_park_cars_to_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(firstParkingLot, secondParkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = smartParkingBoy.park(firstCar);
        ParkingTicket secondTicket = smartParkingBoy.park(secondCar);
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = smartParkingBoy.park(thirdCar);

        assertNull(thirdTicket);
    }

    @Test
    void should_get_message_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(firstParkingLot, secondParkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = smartParkingBoy.park(firstCar);
        ParkingTicket secondTicket = smartParkingBoy.park(secondCar);
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = smartParkingBoy.park(thirdCar);

        assertEquals("The parking lot is full.", smartParkingBoy.getLastErrorMessage());
    }
}
