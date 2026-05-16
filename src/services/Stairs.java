package services;

import models.Flight;
import models.ServiceUnit;

public class Stairs extends ServiceUnit {

    public Stairs(int unitId) {
        super(unitId, 10.0, 100.0, "Stairs");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}