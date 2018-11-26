package com.oocl.cultivation;

public class SuperSmartParkingBoy extends ParkingBoy {

    public SuperSmartParkingBoy(ParkingLot... parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingTicket park(Car car) {
        ParkingLot availableParkingLot = findParkingLotThatHasLargestAvailablePositionRate();
        ParkingTicket ticket = parkCarToParkingLot(car, availableParkingLot);
        return ticket;
    }

    private ParkingLot findParkingLotThatHasLargestAvailablePositionRate(){
        ParkingLot suitableParkingLot = null;
        double maxAvailablePositionRate = 0.0;
        for (ParkingLot parkingLot : this.parkingLots){
            if (parkingLot.getAvailablePositionRate() > maxAvailablePositionRate) {
                maxAvailablePositionRate = parkingLot.getAvailablePositionRate();
                suitableParkingLot = parkingLot;
            }
        }
        return suitableParkingLot;
    }
}
