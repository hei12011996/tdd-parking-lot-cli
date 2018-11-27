package com.oocl.cultivation;

import java.util.*;

public class ParkingBoy extends ParkingPerson{

    protected final List<ParkingLot> parkingLots = new ArrayList<ParkingLot>();
    protected final Map<ParkingTicket, ParkingLot> parkingLotStorage = new HashMap<ParkingTicket, ParkingLot>();
    protected String lastErrorMessage;

    public ParkingBoy(ParkingLot... parkingLots) {
        this.parkingLots.addAll(Arrays.asList(parkingLots));
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
