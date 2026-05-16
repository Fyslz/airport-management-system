package services;

import models.Flight;
import models.ServiceUnit;

public class Ambulance extends ServiceUnit {

    public Ambulance(int unitId) {
        super(unitId, 10.0, 500.0, "Ambulance");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}