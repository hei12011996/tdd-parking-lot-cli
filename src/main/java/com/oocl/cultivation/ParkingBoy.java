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
        return getCarFromParkingLot(ticket);
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    private Car getCarFromParkingLot(ParkingTicket ticket){
        Car fetchedCar = this.parkingLot.returnCar(ticket);
        if (fetchedCar == null) {
            this.lastErrorMessage = "Unrecognized parking ticket.";
        }
        return fetchedCar;
    }
}
