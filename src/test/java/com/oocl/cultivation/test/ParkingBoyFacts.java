package com.oocl.cultivation.test;

import com.oocl.cultivation.Car;
import com.oocl.cultivation.ParkingBoy;
import com.oocl.cultivation.ParkingLot;
import com.oocl.cultivation.ParkingTicket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingBoyFacts {
    @Test
    void should_park_a_car_to_a_parking_lot_and_get_it_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingBoy.park(car);
        Car fetched = parkingBoy.fetch(ticket);

        assertSame(fetched, car);
    }

    @Test
    void should_park_multiple_cars_to_a_parking_lot_and_get_them_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();

        ParkingTicket firstTicket = parkingBoy.park(firstCar);
        ParkingTicket secondTicket = parkingBoy.park(secondCar);

        Car fetchedByFirstTicket = parkingBoy.fetch(firstTicket);
        Car fetchedBySecondTicket = parkingBoy.fetch(secondTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
    }

    @Test
    void should_not_fetch_any_car_once_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingTicket ticket = parkingBoy.park(car);

        assertNull(parkingBoy.fetch(wrongTicket));
        assertSame(car, parkingBoy.fetch(ticket));
    }

    @Test
    void should_query_message_once_the_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingBoy.fetch(wrongTicket);
        String message = parkingBoy.getLastErrorMessage();

        assertEquals("Unrecognized parking ticket.", message);
    }

    @Test
    void should_clear_the_message_once_the_operation_is_succeeded() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingBoy.fetch(wrongTicket);
        assertNotNull(parkingBoy.getLastErrorMessage());

        ParkingTicket ticket = parkingBoy.park(new Car());
        assertNotNull(ticket);
        assertNull(parkingBoy.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_any_car_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingBoy.park(car);

        assertNull(parkingBoy.fetch(null));
        assertSame(car, parkingBoy.fetch(ticket));
    }

    @Test
    void should_query_message_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);

        parkingBoy.fetch(null);

        assertEquals(
            "Please provide your parking ticket.",
            parkingBoy.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_any_car_once_ticket_has_been_used() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingBoy.park(car);
        parkingBoy.fetch(ticket);

        assertNull(parkingBoy.fetch(ticket));
    }

    @Test
    void should_query_error_message_for_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingBoy.park(car);
        parkingBoy.fetch(ticket);
        parkingBoy.fetch(ticket);

        assertEquals(
            "Unrecognized parking ticket.",
            parkingBoy.getLastErrorMessage()
        );
    }

    @Test
    void should_not_park_cars_to_parking_lot_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);

        parkingBoy.park(new Car());

        assertNull(parkingBoy.park(new Car()));
    }

    @Test
    void should_get_message_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);

        parkingBoy.park(new Car());
        parkingBoy.park(new Car());

        assertEquals("The parking lot is full.", parkingBoy.getLastErrorMessage());
    }

    @Test
    void should_park_multiple_cars_to_the_first_parking_lot_as_long_as_it_is_not_full_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot();
        ParkingLot secondParkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(firstParkingLot, secondParkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(10, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = parkingBoy.park(firstCar);
        assertEquals(9, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket secondTicket = parkingBoy.park(secondCar);
        assertEquals(8, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = parkingBoy.park(thirdCar);
        assertEquals(7, firstParkingLot.getAvailableParkingPosition());

        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        Car fetchedByFirstTicket = parkingBoy.fetch(firstTicket);
        Car fetchedBySecondTicket = parkingBoy.fetch(secondTicket);
        Car fetchedByThirdTicket = parkingBoy.fetch(thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }

    @Test
    void should_park_multiple_cars_to_the_second_parking_lot_once_the_first_parking_lot_is_full_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(firstParkingLot, secondParkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = parkingBoy.park(firstCar);
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket secondTicket = parkingBoy.park(secondCar);
        assertEquals(9, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = parkingBoy.park(thirdCar);
        assertEquals(8, secondParkingLot.getAvailableParkingPosition());

        Car fetchedByFirstTicket = parkingBoy.fetch(firstTicket);
        Car fetchedBySecondTicket = parkingBoy.fetch(secondTicket);
        Car fetchedByThirdTicket = parkingBoy.fetch(thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }
}
