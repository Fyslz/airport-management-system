package services;

import models.Flight;
import models.ServiceUnit;

public class PushbackTug extends ServiceUnit {

    public PushbackTug(int unitId) {
        super(unitId, 15.0, 350.0, "PushbackTug");
    }

    @Override
    public void provideService(Flight f) {
        assignToFlight(f); // link service unit with gate and plane
        setAvailable(false); // make unit busy
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGateId());    
        }
    }