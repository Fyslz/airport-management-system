package services;

import models.Flight;
import models.ServiceUnit;

public class CateringTruck extends ServiceUnit {

    public CateringTruck(int unitId) {
        super(unitId, 15.0, 250.0, "CateringTruck");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}