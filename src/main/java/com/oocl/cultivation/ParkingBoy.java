package com.oocl.cultivation;

import java.util.*;

public class ParkingBoy {

    private String lastErrorMessage;
    protected final List<ParkingLot> parkingLots = new ArrayList<ParkingLot>();
    protected final Map<ParkingTicket, ParkingLot> parkingLotStorage = new HashMap<ParkingTicket, ParkingLot>();

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
            setLastErrorMessage(lastErrorMessage = "Please provide your parking ticket.");
        }
        return car;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    private void setLastErrorMessage(String errorMsg){
        this.lastErrorMessage = errorMsg;
    }

    private ParkingTicket parkCarToParkingLot(Car car, ParkingLot parkingLot){
        if (parkingLot == null){
            setLastErrorMessage("The parking lot is full.");
            return null;
        } else {
            clearLastErrorMessage();
            ParkingTicket ticket = parkingLot.parkCar(car);
            this.parkingLotStorage.put(ticket, parkingLot);
            return ticket;
        }
    }

    private Car getCarFromParkingLot(ParkingTicket ticket){
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

    private boolean isTicketProvided(ParkingTicket ticket){
        return ticket != null;
    }

    private void clearLastErrorMessage() {
        this.lastErrorMessage = null;
    }

    private ParkingLot findParkingLotByTicket(ParkingTicket ticket){
        return parkingLotStorage.get(ticket);
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
