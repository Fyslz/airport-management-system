package services;

import models.Flight;
import models.ServiceUnit;

public class SpecialAssistance extends ServiceUnit {

    public SpecialAssistance(int unitId) {
        super(unitId, 15.0, 150.0);
    }

    @Override
    public void provideService(Flight f) {
        System.out.println("SpecialAssistance [" + getUnitId() + "] يقدم المساعدة الخاصة لركاب الرحلة " + f.getFlightId() + "...");
        setAvailable(false);
        moveToGate(f.getAssignedGate());
        try {
            Thread.sleep((long)(getServiceDuration() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        setAvailable(true);
        System.out.println("SpecialAssistance [" + getUnitId() + "] انتهت خدمة المساعدة الخاصة للرحلة " + f.getFlightId() + ".");
    }
}