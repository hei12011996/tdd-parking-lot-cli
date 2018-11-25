package com.oocl.cultivation;

import java.util.*;

public class ParkingBoy {

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
