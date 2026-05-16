package services;

import models.Flight;
import models.ServiceUnit;

public class PassengerBus extends ServiceUnit {

    public PassengerBus(int unitId) {
        super(unitId, 20.0, 200.0, "PassengerBus");
    }

    @Override
    public void provideService(Flight f) {
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGate());
        setAvailable(false);
        moveToGate(f.getAssignedGate());
    }
}