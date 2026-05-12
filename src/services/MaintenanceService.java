package services;

import models.Flight;
import models.ServiceUnit;

public class MaintenanceService extends ServiceUnit {

    public MaintenanceService(int unitId) {
        super(unitId, 60.0, 800.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("MaintenanceService [" + getUnitId() + "] يقوم بصيانة الطائرة " + f.getFlightId() + "...");
        setAvailable(false);
        moveToGate(f.getAssignedGate());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        setAvailable(true);
        System.out.println("MaintenanceService [" + getUnitId() + "] انتهت صيانة الطائرة " + f.getFlightId() + ".");
    }
}