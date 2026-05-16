package services;

import models.Flight;
import models.ServiceUnit;

public class CustomsService extends ServiceUnit {

    public CustomsService(int unitId) {
        super(unitId, 30.0, 400.0, "CustomsService");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}