package com.oocl.cultivation;

public class SmartParkingBoy extends ParkingBoy {

    public SmartParkingBoy(ParkingLot... parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingTicket park(Car car) {
        ParkingLot availableParkingLot = findParkingLotThatContainsMostEmptyPositions();
        ParkingTicket ticket = parkCarToParkingLot(car, availableParkingLot);
        return ticket;
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
