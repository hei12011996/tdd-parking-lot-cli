package com.oocl.cultivation;

public class SuperSmartParkingBoy extends ParkingPerson{

    public SuperSmartParkingBoy(ParkingLot... parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingTicket park(Car car) {
        ParkingLot availableParkingLot = findParkingLotThatHasLargestAvailablePositionRate();
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
