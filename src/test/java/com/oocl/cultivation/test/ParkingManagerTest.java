package com.oocl.cultivation.test;

import com.oocl.cultivation.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingManagerTest {
    @Test
    void should_park_a_car_to_a_parking_lot_and_get_it_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.park(car);
        Car fetched = parkingManager.fetch(ticket);

        assertSame(fetched, car);
    }

    @Test
    void should_park_multiple_cars_to_a_parking_lot_and_get_them_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();

        ParkingTicket firstTicket = parkingManager.park(firstCar);
        ParkingTicket secondTicket = parkingManager.park(secondCar);

        Car fetchedByFirstTicket = parkingManager.fetch(firstTicket);
        Car fetchedBySecondTicket = parkingManager.fetch(secondTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
    }

    @Test
    void should_not_fetch_any_car_once_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        Car car = new Car();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingTicket ticket = parkingManager.park(car);

        assertNull(parkingManager.fetch(wrongTicket));
        assertSame(car, parkingManager.fetch(ticket));
    }

    @Test
    void should_query_message_once_the_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingManager.fetch(wrongTicket);
        String message = parkingManager.getLastErrorMessage();

        assertEquals("Unrecognized parking ticket.", message);
    }

    @Test
    void should_clear_the_message_once_the_operation_is_succeeded() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingManager.fetch(wrongTicket);
        assertNotNull(parkingManager.getLastErrorMessage());

        ParkingTicket ticket = parkingManager.park(new Car());
        assertNotNull(ticket);
        assertNull(parkingManager.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_any_car_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.park(car);

        assertNull(parkingManager.fetch(null));
        assertSame(car, parkingManager.fetch(ticket));
    }

    @Test
    void should_query_message_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);

        parkingManager.fetch(null);

        assertEquals(
                "Please provide your parking ticket.",
                parkingManager.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_any_car_once_ticket_has_been_used() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.park(car);
        parkingManager.fetch(ticket);

        assertNull(parkingManager.fetch(ticket));
    }

    @Test
    void should_query_error_message_for_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.park(car);
        parkingManager.fetch(ticket);
        parkingManager.fetch(ticket);

        assertEquals(
                "Unrecognized parking ticket.",
                parkingManager.getLastErrorMessage()
        );
    }

    @Test
    void should_not_park_cars_to_parking_lot_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingManager parkingManager = new ParkingManager(parkingLot);

        parkingManager.park(new Car());

        assertNull(parkingManager.park(new Car()));
    }

    @Test
    void should_get_message_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingManager parkingManager = new ParkingManager(parkingLot);

        parkingManager.park(new Car());
        parkingManager.park(new Car());

        assertEquals("The parking lot is full.", parkingManager.getLastErrorMessage());
    }

    @Test
    void should_park_multiple_cars_to_the_first_parking_lot_as_long_as_it_is_not_full_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot();
        ParkingLot secondParkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(firstParkingLot, secondParkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(10, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = parkingManager.park(firstCar);
        assertEquals(9, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket secondTicket = parkingManager.park(secondCar);
        assertEquals(8, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = parkingManager.park(thirdCar);
        assertEquals(7, firstParkingLot.getAvailableParkingPosition());

        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        Car fetchedByFirstTicket = parkingManager.fetch(firstTicket);
        Car fetchedBySecondTicket = parkingManager.fetch(secondTicket);
        Car fetchedByThirdTicket = parkingManager.fetch(thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }

    @Test
    void should_park_multiple_cars_to_the_second_parking_lot_once_the_first_parking_lot_is_full_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot();
        ParkingManager parkingManager = new ParkingManager(firstParkingLot, secondParkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = parkingManager.park(firstCar);
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket secondTicket = parkingManager.park(secondCar);
        assertEquals(9, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = parkingManager.park(thirdCar);
        assertEquals(8, secondParkingLot.getAvailableParkingPosition());

        Car fetchedByFirstTicket = parkingManager.fetch(firstTicket);
        Car fetchedBySecondTicket = parkingManager.fetch(secondTicket);
        Car fetchedByThirdTicket = parkingManager.fetch(thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }

    @Test
    void should_not_park_cars_to_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingManager parkingManager = new ParkingManager(firstParkingLot, secondParkingLot);

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        parkingManager.park(new Car());
        parkingManager.park(new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());

        assertNull(parkingManager.park(new Car()));
    }

    @Test
    void should_get_message_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingManager parkingManager = new ParkingManager(firstParkingLot, secondParkingLot);

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        parkingManager.park(new Car());
        parkingManager.park(new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());
        parkingManager.park(new Car());

        assertEquals("The parking lot is full.", parkingManager.getLastErrorMessage());
    }

    @Test
    void should_let_standard_parking_boy_to_park_a_car_to_a_parking_lot_and_get_it_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        Car car = new Car();
        parkingManager.manage(standardParkingBoy);

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(standardParkingBoy, car);
        Car fetched = parkingManager.tellParkingBoyToFetch(standardParkingBoy, ticket);

        assertSame(fetched, car);
    }

    @Test
    void should_let_standard_parking_boy_to_park_multiple_cars_to_a_parking_lot_and_get_them_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        Car firstCar = new Car();
        Car secondCar = new Car();

        ParkingTicket firstTicket = parkingManager.tellParkingBoyToPark(standardParkingBoy, firstCar);
        ParkingTicket secondTicket = parkingManager.tellParkingBoyToPark(standardParkingBoy, secondCar);

        Car fetchedByFirstTicket = parkingManager.tellParkingBoyToFetch(standardParkingBoy, firstTicket);
        Car fetchedBySecondTicket = parkingManager.tellParkingBoyToFetch(standardParkingBoy, secondTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
    }

    @Test
    void should_let_standard_parking_boy_not_fetch_any_car_once_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        Car car = new Car();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(standardParkingBoy, car);

        assertNull(parkingManager.tellParkingBoyToFetch(standardParkingBoy, wrongTicket));
        assertSame(car, parkingManager.tellParkingBoyToFetch(standardParkingBoy, ticket));
    }

    @Test
    void should_let_standard_parking_boy_query_message_once_the_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingManager.tellParkingBoyToFetch(standardParkingBoy, wrongTicket);
        String message = parkingManager.getLastErrorMessageFromParkingBoy();

        assertEquals("Unrecognized parking ticket.", message);
    }

    @Test
    void should_clear_the_message_once_the_let_standard_parking_boy_operation_is_succeeded() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingManager.tellParkingBoyToFetch(standardParkingBoy, wrongTicket);
        assertNotNull(parkingManager.getLastErrorMessageFromParkingBoy());

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());
        assertNotNull(ticket);
        assertNull(parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_standard_parking_boy_not_fetch_any_car_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(standardParkingBoy, car);

        assertNull(parkingManager.tellParkingBoyToFetch(standardParkingBoy,null));
        assertSame(car, parkingManager.tellParkingBoyToFetch(standardParkingBoy, ticket));
    }

    @Test
    void should_query_message_once_ticket_is_not_provided_to_standard_parking_boy() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);

        parkingManager.tellParkingBoyToFetch(standardParkingBoy,null);

        assertEquals(
                "Please provide your parking ticket.",
                parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_standard_parking_boy_not_fetch_any_car_once_ticket_has_been_used() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(standardParkingBoy, car);
        parkingManager.tellParkingBoyToFetch(standardParkingBoy, ticket);

        assertNull(parkingManager.tellParkingBoyToFetch(standardParkingBoy, ticket));
    }

    @Test
    void should_query_error_message_from_standard_parking_boy_for_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(standardParkingBoy, car);
        parkingManager.tellParkingBoyToFetch(standardParkingBoy, ticket);
        parkingManager.tellParkingBoyToFetch(standardParkingBoy, ticket);

        assertEquals(
                "Unrecognized parking ticket.",
                parkingManager.getLastErrorMessageFromParkingBoy()
        );
    }

    @Test
    void should_standard_parking_boy_not_park_cars_to_parking_lot_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);

        parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());

        assertNull(parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car()));
    }

    @Test
    void should_get_message_from_standard_parking_boy_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy standardParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);

        parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());

        assertEquals("The parking lot is full.", parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_standard_parking_boy_park_multiple_cars_to_the_first_parking_lot_as_long_as_it_is_not_full_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot();
        ParkingLot secondParkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(10, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = parkingManager.tellParkingBoyToPark(standardParkingBoy, firstCar);
        assertEquals(9, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket secondTicket = parkingManager.tellParkingBoyToPark(standardParkingBoy, secondCar);
        assertEquals(8, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = parkingManager.tellParkingBoyToPark(standardParkingBoy, thirdCar);
        assertEquals(7, firstParkingLot.getAvailableParkingPosition());

        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        Car fetchedByFirstTicket = parkingManager.tellParkingBoyToFetch(standardParkingBoy, firstTicket);
        Car fetchedBySecondTicket = parkingManager.tellParkingBoyToFetch(standardParkingBoy, secondTicket);
        Car fetchedByThirdTicket = parkingManager.tellParkingBoyToFetch(standardParkingBoy, thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }

    @Test
    void should_standard_parking_boy_park_multiple_cars_to_the_second_parking_lot_once_the_first_parking_lot_is_full_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot();
        ParkingBoy standardParkingBoy = new ParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = parkingManager.tellParkingBoyToPark(standardParkingBoy, firstCar);
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        ParkingTicket secondTicket = parkingManager.tellParkingBoyToPark(standardParkingBoy, secondCar);
        assertEquals(9, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = parkingManager.tellParkingBoyToPark(standardParkingBoy, thirdCar);
        assertEquals(8, secondParkingLot.getAvailableParkingPosition());

        Car fetchedByFirstTicket = parkingManager.tellParkingBoyToFetch(standardParkingBoy, firstTicket);
        Car fetchedBySecondTicket = parkingManager.tellParkingBoyToFetch(standardParkingBoy, secondTicket);
        Car fetchedByThirdTicket = parkingManager.tellParkingBoyToFetch(standardParkingBoy, thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }

    @Test
    void should_standard_parking_boy_not_park_cars_to_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingBoy standardParkingBoy = new ParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());

        assertNull(parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car()));
    }

    @Test
    void should_get_message_from_standard_parking_boy_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingBoy standardParkingBoy = new ParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(standardParkingBoy);

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());
        parkingManager.tellParkingBoyToPark(standardParkingBoy, new Car());

        assertEquals("The parking lot is full.", parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_let_smart_parking_boy_to_park_a_car_to_a_parking_lot_and_get_it_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        Car car = new Car();
        parkingManager.manage(smartParkingBoy);

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(smartParkingBoy, car);
        Car fetched = parkingManager.tellParkingBoyToFetch(smartParkingBoy, ticket);

        assertSame(fetched, car);
    }

    @Test
    void should_let_smart_parking_boy_to_park_multiple_cars_to_a_parking_lot_and_get_them_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);
        Car firstCar = new Car();
        Car secondCar = new Car();

        ParkingTicket firstTicket = parkingManager.tellParkingBoyToPark(smartParkingBoy, firstCar);
        ParkingTicket secondTicket = parkingManager.tellParkingBoyToPark(smartParkingBoy, secondCar);

        Car fetchedByFirstTicket = parkingManager.tellParkingBoyToFetch(smartParkingBoy, firstTicket);
        Car fetchedBySecondTicket = parkingManager.tellParkingBoyToFetch(smartParkingBoy, secondTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
    }

    @Test
    void should_let_smart_parking_boy_not_fetch_any_car_once_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);
        Car car = new Car();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(smartParkingBoy, car);

        assertNull(parkingManager.tellParkingBoyToFetch(smartParkingBoy, wrongTicket));
        assertSame(car, parkingManager.tellParkingBoyToFetch(smartParkingBoy, ticket));
    }

    @Test
    void should_let_smart_parking_boy_query_message_once_the_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingManager.tellParkingBoyToFetch(smartParkingBoy, wrongTicket);
        String message = parkingManager.getLastErrorMessageFromParkingBoy();

        assertEquals("Unrecognized parking ticket.", message);
    }

    @Test
    void should_clear_the_message_once_the_let_smart_parking_boy_operation_is_succeeded() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingManager.tellParkingBoyToFetch(smartParkingBoy, wrongTicket);
        assertNotNull(parkingManager.getLastErrorMessageFromParkingBoy());

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());
        assertNotNull(ticket);
        assertNull(parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_smart_parking_boy_not_fetch_any_car_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(smartParkingBoy, car);

        assertNull(parkingManager.tellParkingBoyToFetch(smartParkingBoy,null));
        assertSame(car, parkingManager.tellParkingBoyToFetch(smartParkingBoy, ticket));
    }

    @Test
    void should_query_message_once_ticket_is_not_provided_to_smart_parking_boy() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);

        parkingManager.tellParkingBoyToFetch(smartParkingBoy,null);

        assertEquals(
                "Please provide your parking ticket.",
                parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_smart_parking_boy_not_fetch_any_car_once_ticket_has_been_used() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(smartParkingBoy, car);
        parkingManager.tellParkingBoyToFetch(smartParkingBoy, ticket);

        assertNull(parkingManager.tellParkingBoyToFetch(smartParkingBoy, ticket));
    }

    @Test
    void should_query_error_message_from_smart_parking_boy_for_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(smartParkingBoy, car);
        parkingManager.tellParkingBoyToFetch(smartParkingBoy, ticket);
        parkingManager.tellParkingBoyToFetch(smartParkingBoy, ticket);

        assertEquals(
                "Unrecognized parking ticket.",
                parkingManager.getLastErrorMessageFromParkingBoy()
        );
    }

    @Test
    void should_smart_parking_boy_not_park_cars_to_parking_lot_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);

        parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());

        assertNull(parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car()));
    }

    @Test
    void should_get_message_from_smart_parking_boy_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);

        parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());

        assertEquals("The parking lot is full.", parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_smart_parking_boy_park_cars_to_parking_lot_which_contains_more_empty_position_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot();
        SmartParkingBoy smartParkingBoy = new SmartParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        ParkingTicket firstTicket = parkingManager.tellParkingBoyToPark(smartParkingBoy, firstCar);
        assertEquals(9, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket secondTicket = parkingManager.tellParkingBoyToPark(smartParkingBoy, secondCar);
        assertEquals(8, secondParkingLot.getAvailableParkingPosition());
        ParkingTicket thirdTicket = parkingManager.tellParkingBoyToPark(smartParkingBoy, thirdCar);
        assertEquals(7, secondParkingLot.getAvailableParkingPosition());

        Car fetchedByFirstTicket = parkingManager.tellParkingBoyToFetch(smartParkingBoy, firstTicket);
        Car fetchedBySecondTicket = parkingManager.tellParkingBoyToFetch(smartParkingBoy, secondTicket);
        Car fetchedByThirdTicket = parkingManager.tellParkingBoyToFetch(smartParkingBoy, thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }
    @Test
    void should_smart_parking_boy_not_park_cars_to_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingBoy smartParkingBoy = new SmartParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());

        assertNull(parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car()));
    }

    @Test
    void should_get_message_from_smart_parking_boy_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingBoy smartParkingBoy = new SmartParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(smartParkingBoy);

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());
        parkingManager.tellParkingBoyToPark(smartParkingBoy, new Car());

        assertEquals("The parking lot is full.", parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_let_super_smart_parking_boy_to_park_a_car_to_a_parking_lot_and_get_it_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        Car car = new Car();
        parkingManager.manage(superSmartParkingBoy);

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, car);
        Car fetched = parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, ticket);

        assertSame(fetched, car);
    }

    @Test
    void should_let_super_smart_parking_boy_to_park_multiple_cars_to_a_parking_lot_and_get_them_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);
        Car firstCar = new Car();
        Car secondCar = new Car();

        ParkingTicket firstTicket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, firstCar);
        ParkingTicket secondTicket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, secondCar);

        Car fetchedByFirstTicket = parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, firstTicket);
        Car fetchedBySecondTicket = parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, secondTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
    }

    @Test
    void should_let_super_smart_parking_boy_not_fetch_any_car_once_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);
        Car car = new Car();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, car);

        assertNull(parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, wrongTicket));
        assertSame(car, parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, ticket));
    }

    @Test
    void should_let_super_smart_parking_boy_query_message_once_the_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, wrongTicket);
        String message = parkingManager.getLastErrorMessageFromParkingBoy();

        assertEquals("Unrecognized parking ticket.", message);
    }

    @Test
    void should_clear_the_message_once_the_let_super_smart_parking_boy_operation_is_succeeded() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, wrongTicket);
        assertNotNull(parkingManager.getLastErrorMessageFromParkingBoy());

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());
        assertNotNull(ticket);
        assertNull(parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_super_smart_parking_boy_not_fetch_any_car_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, car);

        assertNull(parkingManager.tellParkingBoyToFetch(superSmartParkingBoy,null));
        assertSame(car, parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, ticket));
    }

    @Test
    void should_query_message_once_ticket_is_not_provided_to_super_smart_parking_boy() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);

        parkingManager.tellParkingBoyToFetch(superSmartParkingBoy,null);

        assertEquals(
                "Please provide your parking ticket.",
                parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_super_smart_parking_boy_not_fetch_any_car_once_ticket_has_been_used() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, car);
        parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, ticket);

        assertNull(parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, ticket));
    }

    @Test
    void should_query_error_message_from_super_smart_parking_boy_for_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, car);
        parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, ticket);
        parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, ticket);

        assertEquals(
                "Unrecognized parking ticket.",
                parkingManager.getLastErrorMessageFromParkingBoy()
        );
    }

    @Test
    void should_super_smart_parking_boy_not_park_cars_to_parking_lot_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);

        parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());

        assertNull(parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car()));
    }

    @Test
    void should_get_message_from_super_smart_parking_boy_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);

        parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());

        assertEquals("The parking lot is full.", parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_super_smart_parking_boy_park_cars_to_parking_lot_which_has_larger_available_position_rate_and_get_them_back() {
        ParkingLot firstParkingLot = new ParkingLot(5);
        ParkingLot secondParkingLot = new ParkingLot(10);
        SuperSmartParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();

        // Initial available position rate:
        // FirstParkingLot: 5/5 => 100%, Second ParkingLot: 10/10 => 100%
        assertEquals(5, firstParkingLot.getAvailableParkingPosition());
        assertEquals(10, secondParkingLot.getAvailableParkingPosition());

        // Should park to the first parking lot
        ParkingTicket firstTicket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, firstCar);
        assertEquals(4, firstParkingLot.getAvailableParkingPosition());
        // Available position rate after parking the first car:
        // FirstParkingLot: 4/5 => 80%, Second ParkingLot: 10/10 => 100%

        // Should park to the second parking lot
        ParkingTicket secondTicket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, secondCar);
        assertEquals(9, secondParkingLot.getAvailableParkingPosition());
        // Available position rate after parking the second car:
        // FirstParkingLot: 4/5 => 80%, Second ParkingLot: 9/10 => 90%

        // Should still park to the second parking lot
        ParkingTicket thirdTicket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, thirdCar);
        assertEquals(8, secondParkingLot.getAvailableParkingPosition());
        // Available position rate after parking the second car:
        // FirstParkingLot: 4/5 => 80%, Second ParkingLot: 8/10 => 80%

        Car fetchedByFirstTicket = parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, firstTicket);
        Car fetchedBySecondTicket = parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, secondTicket);
        Car fetchedByThirdTicket = parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, thirdTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
        assertSame(thirdCar, fetchedByThirdTicket);
    }

    @Test
    void should_super_smart_parking_boy_not_park_cars_to_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());

        assertNull(parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car()));
    }

    @Test
    void should_get_message_from_super_smart_parking_boy_when_all_parking_lots_are_full() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(firstParkingLot, secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        parkingManager.manage(superSmartParkingBoy);

        assertEquals(1, firstParkingLot.getAvailableParkingPosition());
        assertEquals(1, secondParkingLot.getAvailableParkingPosition());

        parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());
        parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());
        assertEquals(0, firstParkingLot.getAvailableParkingPosition());
        assertEquals(0, secondParkingLot.getAvailableParkingPosition());
        parkingManager.tellParkingBoyToPark(superSmartParkingBoy, new Car());

        assertEquals("The parking lot is full.", parkingManager.getLastErrorMessageFromParkingBoy());
    }

    @Test
    void should_able_to_manage_different_types_of_parking_boy_at_the_same_time() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        ParkingBoy smartParkingBoy = new SmartParkingBoy(parkingLot);
        ParkingBoy superSmartParkingBoy = new SuperSmartParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        Car firstCar = new Car();
        Car secondCar = new Car();
        Car thirdCar = new Car();
        parkingManager.manage(parkingBoy);
        parkingManager.manage(smartParkingBoy);
        parkingManager.manage(superSmartParkingBoy);

        ParkingTicket firstTicket = parkingManager.tellParkingBoyToPark(parkingBoy, firstCar);
        ParkingTicket secondTicket = parkingManager.tellParkingBoyToPark(smartParkingBoy, secondCar);
        ParkingTicket thirdTicket = parkingManager.tellParkingBoyToPark(superSmartParkingBoy, thirdCar);
        assertEquals(7, parkingLot.getAvailableParkingPosition());

        Car fetchedFirstCar = parkingManager.tellParkingBoyToFetch(parkingBoy, firstTicket);
        Car fetchedSecondCar = parkingManager.tellParkingBoyToFetch(smartParkingBoy, secondTicket);
        Car fetchedThirdCar = parkingManager.tellParkingBoyToFetch(superSmartParkingBoy, thirdTicket);

        assertSame(firstCar, fetchedFirstCar);
        assertSame(secondCar, fetchedSecondCar);
        assertSame(thirdCar, fetchedThirdCar);
    }

    @Test
    void should_not_park_car_when_telling_a_parking_boy_who_not_under_management_to_park_car() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy unknownParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        Car car = new Car();

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(unknownParkingBoy, car);

        assertNull(ticket);
    }

    @Test
    void should_query_message_when_telling_a_parking_boy_who_not_under_management_to_park_car() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy unknownParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager();
        Car car = new Car();

        parkingManager.tellParkingBoyToPark(unknownParkingBoy, car);

        assertEquals("That parking boy is not under management.", parkingManager.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_car_when_telling_a_parking_boy_who_not_under_management_to_fetch() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy unknownParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.park(car);

        assertNull(parkingManager.tellParkingBoyToFetch(unknownParkingBoy, ticket));
    }

    @Test
    void should_query_message_when_telling_a_parking_boy_who_not_under_management_to_fetch() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy unknownParkingBoy = new ParkingBoy(parkingLot);
        ParkingManager parkingManager = new ParkingManager(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingManager.park(car);
        parkingManager.tellParkingBoyToFetch(unknownParkingBoy, ticket);

        assertEquals("That parking boy is not under management.", parkingManager.getLastErrorMessage());
    }

    @Test
    void should_not_fetch_car_when_telling_a_parking_boy_to_fetch_car_from_a_parking_lot_which_not_under_his_management() {
        ParkingLot firstParkingLot = new ParkingLot();
        ParkingLot secondParkingLot = new ParkingLot();
        ParkingBoy firstParkingBoy = new ParkingBoy(firstParkingLot);
        ParkingBoy secondParkingBoy = new ParkingBoy(secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        Car car = new Car();
        parkingManager.manage(firstParkingBoy);
        parkingManager.manage(secondParkingBoy);

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(firstParkingBoy, car);

        assertNull(parkingManager.tellParkingBoyToFetch(secondParkingBoy, ticket));
    }

    @Test
    void should_query_message_when_telling_a_parking_boy_to_fetch_car_from_a_parking_lot_which_not_under_his_management() {
        ParkingLot firstParkingLot = new ParkingLot();
        ParkingLot secondParkingLot = new ParkingLot();
        ParkingBoy firstParkingBoy = new ParkingBoy(firstParkingLot);
        ParkingBoy secondParkingBoy = new ParkingBoy(secondParkingLot);
        ParkingManager parkingManager = new ParkingManager();
        Car car = new Car();
        parkingManager.manage(firstParkingBoy);
        parkingManager.manage(secondParkingBoy);

        ParkingTicket ticket = parkingManager.tellParkingBoyToPark(firstParkingBoy, car);
        parkingManager.tellParkingBoyToFetch(secondParkingBoy, ticket);

        assertEquals("Unrecognized parking ticket.", parkingManager.getLastErrorMessageFromParkingBoy());
    }
}
