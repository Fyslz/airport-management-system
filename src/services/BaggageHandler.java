package services;

import models.Flight;
import models.ServiceUnit;

public class BaggageHandler extends ServiceUnit {

    public BaggageHandler(int unitId) {
        super(unitId, 20.0, 300.0, "BaggageHandler");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}