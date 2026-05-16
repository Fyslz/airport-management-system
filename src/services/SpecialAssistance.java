package services;

import models.Flight;
import models.ServiceUnit;

public class SpecialAssistance extends ServiceUnit {

    public SpecialAssistance(int unitId) {
        super(unitId, 15.0, 150.0, "SpecialAssistance");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}