package models;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceUnit {
    private int unitId;
    private double serviceDuration;
    private static int availableServiceUnits = 0;
    private boolean isAvailable;
    private double cost;
    private int currentGate;
    private String serviceType; // to calculate how many service we got in each (Airport class)

    private Flight activePlaneServing;
    private List<Flight> planesServedHistory = new ArrayList<>();
    private Gate activeOnGate; 
    private List<Gate> GateServedHistory = new ArrayList<>();
    
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

    public void assignToFlight(Flight f) {
        this.activePlaneServing = f;
        this.activeOnGate = f.getAssignedGate(); // taking gate from plane
        
        this.planesServedHistory.add(f);
        this.GateServedHistory.add(f.getAssignedGate());
        
    }

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

    public String getServiceType() {return serviceType;
        
    }

    public Gate getActiveOnGate() {
        return activeOnGate;
    }
    
    public List<Gate> getGateServedHistory() {
        return GateServedHistory;
    }

    public Flight getActivePlaneServing() {
        return activePlaneServing;
    }

    public List<Flight> getPlanesServedHistory() {
        return planesServedHistory;
    }
    
}
