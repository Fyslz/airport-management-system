package services;

import models.Flight;
import models.ServiceUnit;

public class CustomsService extends ServiceUnit {

    public CustomsService(int unitId) {
        super(unitId, 30.0, 400.0, "CustomsService");
    }

    @Override
    public void provideService(Flight f) {
        assignToFlight(f); // link service unit with gate and plane
        setAvailable(false); // make unit busy
        System.out.println(getServiceType() + "[" + getUnitId() + "] is providing service to flight [" + f.getFlightId() + "] at gate :" + f.getAssignedGateId());    
        }
    }