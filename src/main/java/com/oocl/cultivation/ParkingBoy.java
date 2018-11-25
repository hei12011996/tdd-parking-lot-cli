package com.oocl.cultivation;

public class ParkingBoy {

    private final ParkingLot parkingLot;
    private String lastErrorMessage;

    public ParkingBoy(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public ParkingTicket park(Car car) {
        return this.parkingLot.parkCar(car);
    }

    public Car fetch(ParkingTicket ticket) {
        return this.parkingLot.returnCar(ticket);
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
