package com.oocl.cultivation.test;

import com.oocl.cultivation.Car;
import com.oocl.cultivation.SuperSmartParkingBoy;
import com.oocl.cultivation.ParkingLot;
import com.oocl.cultivation.ParkingTicket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SuperSmartParkingBoyTest {
    @Test
    void should_park_a_car_to_a_parking_lot_and_get_it_back() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = superSmartParkingBoy.park(car);
        Car fetched = superSmartParkingBoy.fetch(ticket);

        assertSame(fetched, car);
    }

    @Test
    void should_park_multiple_cars_to_a_parking_lot_and_get_them_back() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();

        ParkingTicket firstTicket = superSmartParkingBoy.park(firstCar);
        ParkingTicket secondTicket = superSmartParkingBoy.park(secondCar);

        Car fetchedByFirstTicket = superSmartParkingBoy.fetch(firstTicket);
        Car fetchedBySecondTicket = superSmartParkingBoy.fetch(secondTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
    }

    @Test
    void should_not_fetch_any_car_once_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        Car car = new Car();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingTicket ticket = superSmartParkingBoy.park(car);

        assertNull(superSmartParkingBoy.fetch(wrongTicket));
        assertSame(car, superSmartParkingBoy.fetch(ticket));
    }

    @Test
    void should_query_message_once_the_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        superSmartParkingBoy.fetch(wrongTicket);
        String message = superSmartParkingBoy.getLastErrorMessage();

        assertEquals("Unrecognized parking ticket.", message);
    }

    @Test
    void should_clear_the_message_once_the_operation_is_succeeded() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        superSmartParkingBoy.fetch(wrongTicket);
        assertNotNull(superSmartParkingBoy.getLastErrorMessage());

        ParkingTicket ticket = superSmartParkingBoy.park(new Car());
        assertNotNull(ticket);
        assertNull(superSmartParkingBoy.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_any_car_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = superSmartParkingBoy.park(car);

        assertNull(superSmartParkingBoy.fetch(null));
        assertSame(car, superSmartParkingBoy.fetch(ticket));
    }

    @Test
    void should_query_message_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);

        superSmartParkingBoy.fetch(null);

        assertEquals(
                "Please provide your parking ticket.",
                superSmartParkingBoy.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_any_car_once_ticket_has_been_used() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = superSmartParkingBoy.park(car);
        superSmartParkingBoy.fetch(ticket);

        assertNull(superSmartParkingBoy.fetch(ticket));
    }

    @Test
    void should_query_error_message_for_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = superSmartParkingBoy.park(car);
        superSmartParkingBoy.fetch(ticket);
        superSmartParkingBoy.fetch(ticket);

        assertEquals(
                "Unrecognized parking ticket.",
                superSmartParkingBoy.getLastErrorMessage()
        );
    }

    @Test
    void should_not_park_cars_to_parking_lot_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);

        superSmartParkingBoy.park(new Car());

        assertNull(superSmartParkingBoy.park(new Car()));
    }

    @Test
    void should_get_message_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);

        superSmartParkingBoy.park(new Car());
        superSmartParkingBoy.park(new Car());

        assertEquals("The parking lot is full.", superSmartParkingBoy.getLastErrorMessage());
    }

    @Test
    void should_park_cars_to_parking_lot_which_has_larger_available_position_rate_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot(5);
        ParkingLot secondParkingLot = new ParkingLot(10);
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(firstParkingLot, secondParkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        // Initial available position rate:
        // FirstParkingLot: 5/5 => 100%, Second ParkingLot: 10/10 => 100%
        assertEquals(5, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        // Should park to the first parking lot
        ParkingTicket firstTicket = superSmartParkingBoy.park(firstCar);
        assertEquals(4, firstParkingLot.getAvailableParkingPosition());
        // Available position rate after parking the first car:
        // FirstParkingLot: 4/5 => 80%, Second ParkingLot: 10/10 => 100%

        // Should park to the second parking lot
        ParkingTicket secondTicket = superSmartParkingBoy.park(secondCar);
        assertEquals(9, secondParkingLot.getAvailableParkingPosition());
        // Available position rate after parking the second car:
        // FirstParkingLot: 4/5 => 80%, Second ParkingLot: 9/10 => 90%

        // Should still park to the second parking lot
        ParkingTicket thirdTicket = superSmartParkingBoy.park(thirdCar);
        assertEquals(8, secondParkingLot.getAvailableParkingPosition());
        // Available position rate after parking the second car:
        // FirstParkingLot: 4/5 => 80%, Second ParkingLot: 8/10 => 80%

        Car fetchedByFirstTicket = superSmartParkingBoy.fetch(firstTicket);
        Car fetchedBySecondTicket = superSmartParkingBoy.fetch(secondTicket);
        Car fetchedByThirdTicket = superSmartParkingBoy.fetch(thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }

    @Test
    void should_not_park_cars_to_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(firstParkingLot, secondParkingLot);
        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        superSmartParkingBoy.park(new Car());
        superSmartParkingBoy.park(new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());

        assertNull(superSmartParkingBoy.park(new Car()));
    }

    @Test
    void should_get_message_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(firstParkingLot, secondParkingLot);
        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        superSmartParkingBoy.park(new Car());
        superSmartParkingBoy.park(new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());
        superSmartParkingBoy.park(new Car());

        assertEquals("The parking lot is full.", superSmartParkingBoy.getLastErrorMessage());
    }
}
