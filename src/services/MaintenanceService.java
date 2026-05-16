package services;

import models.Flight;
import models.ServiceUnit;

public class MaintenanceService extends ServiceUnit {

    public MaintenanceService(int unitId) {
        super(unitId, 60.0, 800.0, "MaintenanceService");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}