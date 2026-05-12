package services;

import models.Flight;
import models.ServiceUnit;

public class Stairs extends ServiceUnit {

    public Stairs(int unitId) {
        super(unitId, 10.0, 100.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("Stairs [" + getUnitId() + "] يتم توصيل السلم للطائرة " + f.getFlightId() + "...");
        setAvailable(false);
        moveToGate(f.getAssignedGate());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        setAvailable(true);
        System.out.println("Stairs [" + getUnitId() + "] تم سحب السلم من الطائرة " + f.getFlightId() + ".");
    }
}