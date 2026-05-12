package services;

import models.Flight;
import models.ServiceUnit;

public class CateringTruck extends ServiceUnit {

    public CateringTruck(int unitId) {
        super(unitId, 15.0, 250.0);
    }

    @Override
    public void provideService(Flight f) {
        moveToGate(f.getAssignedGate());
        setAvailable(false);
        System.out.println("CateringTruck " + getUnitId() +
            " delivering catering for flight " +
            f.getFlightId());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setAvailable(true);
    }
}