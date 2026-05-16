package services;

import models.Flight;
import models.ServiceUnit;

public class PushbackTug extends ServiceUnit {

    public PushbackTug(int unitId) {
        super(unitId, 15.0, 350.0, "PushbackTug");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}