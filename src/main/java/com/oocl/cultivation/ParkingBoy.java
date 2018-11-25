package com.oocl.cultivation;

public class ParkingBoy {

    private final ParkingLot parkingLot;
    private String lastErrorMessage;

    public ParkingBoy(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public ParkingTicket park(Car car) {
        clearLastErrorMessage();
        return this.parkingLot.parkCar(car);
    }

    public Car fetch(ParkingTicket ticket) {
        if (isTicketValid(ticket)){
            return getCarFromParkingLot(ticket);
        } else {
            this.lastErrorMessage = "Please provide your parking ticket.";
            return null;
        }
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    private boolean isTicketValid(ParkingTicket ticket){
        return ticket != null;
    }

    private Car getCarFromParkingLot(ParkingTicket ticket){
        Car fetchedCar = this.parkingLot.returnCar(ticket);
        if (fetchedCar == null) {
            this.lastErrorMessage = "Unrecognized parking ticket.";
        } else {
            clearLastErrorMessage();
        }
        return fetchedCar;
    }

    private void clearLastErrorMessage() {
        this.lastErrorMessage = null;
    }
}
