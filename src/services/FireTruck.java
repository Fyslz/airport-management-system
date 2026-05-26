package services;

import models.Flight;
import models.ServiceUnit;

public class FireTruck extends ServiceUnit {

    public FireTruck(int unitId) {
        super(unitId, 5.0, 600.0, "FireTruck");
    }

    @Override
    public void provideService(Flight f) {
        assignToFlight(f); // link service unit with gate and plane
        setAvailable(false); // make unit busy
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGateId());    
        }
    }