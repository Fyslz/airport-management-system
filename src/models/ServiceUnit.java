package models;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceUnit {

    // ************************************************
    //  1. DATA FIELD / VARIABLES
    // ************************************************

    private static int availableServiceUnits = 0; // Total available services across all units

    // Unit Info
    private int unitId;
    private String serviceType; 
    private double serviceDuration;
    private double cost;
    private boolean isAvailable;
    private int currentGateId; // -1 indicates unassigned gate
    private Flight activePlaneServing;
    private Gate currentGate; 
    
    // History Lists
    private List<Flight> planesServedHistory = new ArrayList<>();
    private List<Gate> gateServedHistory = new ArrayList<>(); 


    // ************************************************
    //  2. Constructors
    // ************************************************
    public ServiceUnit(int unitId, double serviceDuration, double cost, String serviceType) {
        this.unitId = unitId;
        this.serviceDuration = serviceDuration;
        this.cost = cost;
        this.serviceType = serviceType; 
        
        this.isAvailable = true;
        this.currentGateId = -1; // Unassigned by default

        availableServiceUnits++; // Increment total available services upon creation
    }


    // ************************************************
    //  3. Methods
    // ************************************************
    
    // Abstract method meant to be implemented by child classes
    public abstract void provideService(Flight f);

    public void assignToFlight(Flight f) {
        // Assign service to Plane
        this.activePlaneServing = f;
        this.currentGate = f.getAssignedGate(); // Get gate from the assigned plane
        
        // Add to history records
        this.planesServedHistory.add(f);
        this.gateServedHistory.add(f.getAssignedGate());
    }

    public void moveToGate(Gate gate) {
        this.currentGate = gate;
    }


    // ************************************************
    //  4. Encapsulation Methods
    // ************************************************

    // ======================== Static Method ==========================
    public static int getAvailableServiceUnits() { return availableServiceUnits; }
    // =================================================================


    // =========================== Unit Info ===========================
    public int getUnitId() { return unitId; }
    public String getServiceType() { return serviceType; }
    public double getServiceDuration() { return serviceDuration; }
    // =================================================================


    // ======================= Status & Location =======================
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) {
        if (this.isAvailable != available) {
            this.isAvailable = available;
            
            // Adjust the static counter based on availability
            if (available) {
                availableServiceUnits++;
            } else {
                availableServiceUnits--;
            }
        }
    }
    // =================================================================


    // ======================= Cost Calculations =======================    
    public double getCost() { return cost; }
    // =================================================================


    // ========================= Flight & Gate =========================
    public int getCurrentGateId() { return currentGateId; }
    public Flight getActivePlaneServing() { return activePlaneServing; }
    public Gate getCurrentGate() { return currentGate; }
    public List<Flight> getPlanesServedHistory() { return planesServedHistory; }
    public List<Gate> getGateServedHistory() { return gateServedHistory; }
    // =================================================================
    
}