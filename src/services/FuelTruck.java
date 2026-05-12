package services;

import models.Flight;
import models.ServiceUnit;

public class FuelTruck extends ServiceUnit {

    public FuelTruck(int unitId) {
        super(unitId, 20.0, 500.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("FuelTruck [" + getUnitId() + "] يقوم بتزويد الرحلة " + f.getFlightId() + " بالوقود...");
        setAvailable(false);
        moveToGate(f.getAssignedGate());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        setAvailable(true);
        System.out.println("FuelTruck [" + getUnitId() + "] انتهى من تزويد الرحلة " + f.getFlightId() + " بالوقود.");
    }
}