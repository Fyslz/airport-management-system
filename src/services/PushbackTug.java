package services;

import models.Flight;
import models.ServiceUnit;

public class PushbackTug extends ServiceUnit {

    public PushbackTug(int unitId) {
        super(unitId, 15.0, 350.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("PushbackTug [" + getUnitId() + "] يقوم بدفع الطائرة " + f.getFlightId() + " للخلف...");
        setAvailable(false);
        moveToGate(f.getAssignedGate());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        setAvailable(true);
        System.out.println("PushbackTug [" + getUnitId() + "] انتهى من دفع الطائرة " + f.getFlightId() + ".");
    }
}