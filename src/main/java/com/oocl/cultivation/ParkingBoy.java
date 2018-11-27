package com.oocl.cultivation;

import java.util.*;

public class ParkingBoy extends ParkingPerson{

    public ParkingBoy(ParkingLot... parkingLots) {
        super(parkingLots);
    }

    public ParkingTicket park(Car car) {
        ParkingLot availableParkingLot = findAvailableParkingLotSequentially();
        ParkingTicket ticket = parkCarToParkingLot(car, availableParkingLot);
        return ticket;
    }

    private ParkingLot findAvailableParkingLotSequentially(){
        ParkingLot availableParkingLot = null;
        for (ParkingLot parkingLot : this.parkingLots){
            if (parkingLot.getAvailableParkingPosition() > 0) {
                availableParkingLot = parkingLot;
                break;
            }
        }
        return availableParkingLot;
    }
}
