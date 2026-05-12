package services;

import models.Flight;
import models.ServiceUnit;

public class FireTruck extends ServiceUnit {

    public FireTruck(int unitId) {
        super(unitId, 5.0, 600.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("FireTruck " + getUnitId() +
            " on standby for flight " +
            f.getFlightId());
        setAvailable(false);
    }
}