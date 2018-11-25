package com.oocl.cultivation;

import java.util.*;

public abstract class ParkingPerson {

    protected final List<ParkingLot> parkingLots = new ArrayList<ParkingLot>();
    protected final Map<ParkingTicket, ParkingLot> parkingLotStorage = new HashMap<ParkingTicket, ParkingLot>();
    protected String lastErrorMessage;

    protected ParkingPerson(ParkingLot... parkingLots) {
        this.parkingLots.addAll(Arrays.asList(parkingLots));
    }

    public abstract ParkingTicket park(Car car);

    public abstract Car fetch(ParkingTicket ticket);

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    protected void setLastErrorMessage(String errorMsg){
        this.lastErrorMessage = errorMsg;
    }

    protected ParkingTicket parkCarToParkingLot(Car car, ParkingLot parkingLot){
        if (parkingLot == null || parkingLot.getAvailableParkingPosition() == 0){
            setLastErrorMessage("The parking lot is full.");
            return null;
        } else {
            clearLastErrorMessage();
            ParkingTicket ticket = parkingLot.parkCar(car);
            this.parkingLotStorage.put(ticket, parkingLot);
            return ticket;
        }
    }

    protected Car getCarFromParkingLot(ParkingTicket ticket){
        ParkingLot parkingLot = findParkingLotByTicket(ticket);
        if (parkingLot == null) {
            setLastErrorMessage("Unrecognized parking ticket.");
            return null;
        } else {
            clearLastErrorMessage();
            this.parkingLotStorage.remove(ticket);
        }
        return parkingLot.returnCar(ticket);
    }

    protected boolean isTicketProvided(ParkingTicket ticket){
        return ticket != null;
    }

    protected void clearLastErrorMessage() {
        this.lastErrorMessage = null;
    }

    protected ParkingLot findParkingLotByTicket(ParkingTicket ticket){
        return parkingLotStorage.get(ticket);
    }
}
