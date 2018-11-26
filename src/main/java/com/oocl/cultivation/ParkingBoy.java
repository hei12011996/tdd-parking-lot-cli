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
        ParkingLot availableParkingLot = findAvailableParkingLot();
        ParkingTicket ticket = parkCarToParkingLot(car, availableParkingLot);
        return ticket;
    }

    public Car fetch(ParkingTicket ticket) {
        Car car = null;
        if (isTicketProvided(ticket)){
            car = getCarFromParkingLot(ticket);
        } else {
            setLastErrorMessage("Please provide your parking ticket.");
        }
        return car;
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
}
