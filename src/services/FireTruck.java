package services;

import models.Flight;
import models.ServiceUnit;

public class FireTruck extends ServiceUnit {

    public FireTruck(int unitId) {
        super(unitId, 5.0, 600.0);
    }

    @Override
    public void provideService(Flight f) {
        moveToGate(f.getAssignedGate());
        setAvailable(false);
        System.out.println("FireTruck " + getUnitId() +
            " on standby for flight " +
            f.getFlightId());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setAvailable(true);
    }
}