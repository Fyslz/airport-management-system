package services;

import models.Flight;
import models.ServiceUnit;

public class CleaningCrew extends ServiceUnit {

    public CleaningCrew(int unitId) {
        super(unitId, 25.0, 200.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("CleaningCrew " + getUnitId() +
            " cleaning flight " +
            f.getFlightId());
        setAvailable(false);
    }
}