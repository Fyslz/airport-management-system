package services;

import models.Flight;
import models.ServiceUnit;

public class BaggageHandler extends ServiceUnit {

    public BaggageHandler(int unitId) {
        super(unitId, 20.0, 300.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("BaggageHandler " + getUnitId() +
            " handling baggage for flight " +
            f.getFlightId());
        setAvailable(false);
    }
}