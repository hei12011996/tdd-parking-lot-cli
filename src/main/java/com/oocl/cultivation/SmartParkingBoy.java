package com.oocl.cultivation;

public class SmartParkingBoy extends ParkingPerson {

    public SmartParkingBoy(ParkingLot... parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingTicket park(Car car) {
        ParkingLot availableParkingLot = findParkingLotThatContainsMostEmptyPositions();
        ParkingTicket ticket = parkCarToParkingLot(car, availableParkingLot);
        return ticket;
    }

    @Override
    public Car fetch(ParkingTicket ticket) {
        Car car = null;
        if (isTicketProvided(ticket)){
            car = getCarFromParkingLot(ticket);
        } else {
            setLastErrorMessage("Please provide your parking ticket.");
        }
        return car;
    }

    private ParkingLot findParkingLotThatContainsMostEmptyPositions(){
        ParkingLot mostEmptyPositionsParkingLot = null;
        int maxNumberOfEmptyPositions = 0;
        for (ParkingLot parkingLot : this.parkingLots){
            if (parkingLot.getAvailableParkingPosition() > maxNumberOfEmptyPositions) {
                maxNumberOfEmptyPositions = parkingLot.getAvailableParkingPosition();
                mostEmptyPositionsParkingLot = parkingLot;
            }
        }
        return mostEmptyPositionsParkingLot;
    }
}
