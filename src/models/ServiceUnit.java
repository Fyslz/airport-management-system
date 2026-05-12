package models;

public abstract class ServiceUnit {
    private int unitId;
    private double serviceDuration;
    private static int availableServiceUnits;
    private boolean isAvailable;
    private double cost;
    private int currentGate;

    public ServiceUnit(int unitId, double serviceDuration, double cost) {
        this.unitId = unitId;
        this.serviceDuration = serviceDuration;
        this.cost = cost;
        this.isAvailable = true;
        this.currentGate = -1;
        availableServiceUnits++;
    }

    public abstract void provideService(Flight f);

    public void moveToGate(int gateId) {
        this.currentGate = gateId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
        if (available) availableServiceUnits++;
        else availableServiceUnits--;
    }

    public double getCost() {
        return cost;
    }

    public double getServiceDuration() {
        return serviceDuration;
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
}