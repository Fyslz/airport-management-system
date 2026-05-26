package services;

import models.Flight;
import models.ServiceUnit;

public class CleaningCrew extends ServiceUnit {

    public CleaningCrew(int unitId) {
        super(unitId, 25.0, 200.0, "CleaningCrew");
    }

    @Override
    public void provideService(Flight f) {
        assignToFlight(f); // link service unit with gate and plane
        setAvailable(false); // make unit busy
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGateId());    
        }
    }