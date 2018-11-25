package com.oocl.cultivation;

public class SuperSmartParkingBoy extends ParkingPerson{

    public SuperSmartParkingBoy(ParkingLot... parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingTicket park(Car car) {
        return null;
    }

    @Override
    public Car fetch(ParkingTicket ticket) {
        return null;
    }
}
