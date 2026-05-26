package services;

import models.Flight;
import models.ServiceUnit;

public class Ambulance extends ServiceUnit {

    public Ambulance(int unitId) {
        super(unitId, 10.0, 500.0, "Ambulance");
    }

    @Override
    public void provideService(Flight f) {
        assignToFlight(f); // link service unit with gate and plane
        setAvailable(false); // make unit busy
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGateId());    
        }
    }