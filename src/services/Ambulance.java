package services;

import models.Flight;
import models.ServiceUnit;

public class Ambulance extends ServiceUnit {

    public Ambulance(int unitId) {
        super(unitId, 10.0, 500.0);
    }

    @Override
    public void provideService(Flight f) {
        moveToGate(f.getAssignedGate());
        setAvailable(false);
        System.out.println("Ambulance " + getUnitId() +
            " providing emergency medical service for flight " +
            f.getFlightId());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setAvailable(true);
    }
}