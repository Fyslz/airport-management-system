package models;

public abstract class ServiceUnit {
    private int unitId;
    private double serviceDuration;
    private static int availableServiceUnits = 0;
    private boolean isAvailable;
    private double cost;
    private int currentGate;
    private String serviceType; // to calculate how many service we got in each (Airport class)

    public ServiceUnit(int unitId, double serviceDuration, double cost, String serviceType) {
        this.unitId = unitId;
        this.serviceDuration = serviceDuration;
        this.cost = cost;
        this.serviceType = serviceType; // how many service we have of this service unit
        this.isAvailable = true;
        this.currentGate = -1; // indicates unassigned gate
        availableServiceUnits++; // total available services (ignore type)
    }

    public abstract void provideService(Flight f);

    public void moveToGate(int gateId) {
        this.currentGate = gateId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double getCost() {
        return cost;
    }

    public double getServiceDuration() {
        return serviceDuration;
    }

    // -------------------------------------
    // Additional Methods (Added by Ahmed)
    // -------------------------------------


    public void setAvailable(boolean available) {
        if (this.isAvailable != available){
            this.isAvailable = available;
            if (available) availableServiceUnits++;
            else availableServiceUnits--;
        }
    }
    public int getUnitId() {
        return unitId;
    }

    public int getCurrentGate() {
        return currentGate;
    }

    public static int getAvailableServiceUnits() {
        return availableServiceUnits;
    }


    // -------------------------------------
    // Additional Methods (Added by Fysl)
    // -------------------------------------

    public String getServiceType() {return serviceType;}

}
