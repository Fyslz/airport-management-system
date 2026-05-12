package services;

import models.Flight;
import models.ServiceUnit;

public class CateringTruck extends ServiceUnit {

    public CateringTruck(int unitId) {
        super(unitId, 15.0, 250.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("CateringTruck " + getUnitId() +
            " delivering catering for flight " +
            f.getFlightId());
        setAvailable(false);
    }
}