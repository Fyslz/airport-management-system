package services;

import models.Flight;
import models.ServiceUnit;

public class PassengerBus extends ServiceUnit {

    public PassengerBus(int unitId) {
        super(unitId, 20.0, 200.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("PassengerBus [" + getUnitId() + "] يقوم بنقل ركاب الرحلة " + f.getFlightId() + "...");
        setAvailable(false);
        moveToGate(f.getAssignedGate());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        setAvailable(true);
        System.out.println("PassengerBus [" + getUnitId() + "] انتهى من نقل ركاب الرحلة " + f.getFlightId() + ".");
    }
}