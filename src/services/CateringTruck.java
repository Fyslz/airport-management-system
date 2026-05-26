package services;

import models.Flight;
import models.ServiceUnit;

public class CateringTruck extends ServiceUnit {

    public CateringTruck(int unitId) {
        super(unitId, 15.0, 250.0, "CateringTruck");
    }

    @Override
    public void provideService(Flight f) {
        assignToFlight(f); // link service unit with gate and plane
        setAvailable(false); // make unit busy
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGateId());    
        }
    }