package com.oocl.cultivation;

import java.util.ArrayList;
import java.util.List;

public class ParkingManager extends ParkingPerson {

    private final List<ParkingBoy> employees = new ArrayList<ParkingBoy>();
    private String lastErrorMessageFromParkingBoy;

    public ParkingManager(ParkingLot... parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingTicket park(Car car) {
        ParkingLot availableParkingLot = findAvailableParkingLot();
        ParkingTicket ticket = parkCarToParkingLot(car, availableParkingLot);
        return ticket;
    }

    public ParkingTicket tellParkingBoyToPark(ParkingBoy parkingBoy, Car car) {
        if (!isParkingBoyUnderManagement(parkingBoy)) {
            setLastErrorMessage("That parking boy is not under management.");
            return null;
        } else {
            ParkingTicket ticket = parkingBoy.park(car);
            setLastErrorMessageFromParkingBoy(parkingBoy.getLastErrorMessage());
            return ticket;
        }
    }

    public Car tellParkingBoyToFetch(ParkingBoy parkingBoy, ParkingTicket ticket) {
        if (!isParkingBoyUnderManagement(parkingBoy)) {
            setLastErrorMessage("That parking boy is not under management.");
            return null;
        } else {
            Car car = parkingBoy.fetch(ticket);
            setLastErrorMessageFromParkingBoy(parkingBoy.getLastErrorMessage());
            return car;
        }
    }

    public String getLastErrorMessageFromParkingBoy() {
        return lastErrorMessageFromParkingBoy;
    }

    protected void setLastErrorMessageFromParkingBoy(String errorMsg){
        this.lastErrorMessageFromParkingBoy = errorMsg;
    }

    public void manage(ParkingBoy parkingBoy){
        employees.add(parkingBoy);
    }

    private ParkingLot findAvailableParkingLot(){
        ParkingLot availableParkingLot = null;
        for (ParkingLot parkingLot : this.parkingLots){
            if (parkingLot.getAvailableParkingPosition() > 0) {
                availableParkingLot = parkingLot;
                break;
            }
        }
        return availableParkingLot;
    }

    private boolean isParkingBoyUnderManagement(ParkingBoy parkingBoy){
        return this.employees.contains(parkingBoy);
    }
}
