package services;

import models.Flight;
import models.ServiceUnit;

public class CustomsService extends ServiceUnit {

    public CustomsService(int unitId) {
        super(unitId, 30.0, 400.0);
    }

    @Override
    public void provideService(Flight f) {
        moveToGate(f.getAssignedGate());
        setAvailable(false);
        System.out.println("CustomsService " + getUnitId() +
            " processing customs for flight " +
            f.getFlightId());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setAvailable(true);
    }
}