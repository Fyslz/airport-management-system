package services;

import models.Flight;
import models.ServiceUnit;

public class FuelTruck extends ServiceUnit {

    public FuelTruck(int unitId) {
        super(unitId, 20.0, 500.0,"FuelTruck");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}