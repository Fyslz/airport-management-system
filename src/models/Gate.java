package models;

import java.util.ArrayList;
import java.util.List;

public class Gate {

    // ************************************************
    //  1. DATA FIELD / VARIABLES
    // ************************************************
    private int gateId;
    private boolean isAvailable;
    private Flight activePlaneOnGate; // List of planes that parked on this gate

    
    // DRY : Don't Repeat Yourself, Using this Flight Objects We can extract everything detail we need
    private List<Flight> historyPlanesOnGate = new ArrayList<>(); // History of all planes that parked on the gate
    private List<ServiceUnit> UnitsServedOnGate = new ArrayList<>(); // History of all units that served on the gate


    // ************************************************
    //  2. Constructors
    // ************************************************
    public Gate(int gateId) {
        this.gateId = gateId; // Gate number    
        this.isAvailable = true; // Free by default 
        this.activePlaneOnGate = null;
    }


    // ************************************************
    //  3. Methods
    // ************************************************
        
    // Planes Management 
    public void addPlaneToGate(Flight f) {
        this.activePlaneOnGate = f; // Park plane to gate
        setAvailable(false); // Close gate
        this.addPlaneToGateHistory(f); // Add plane to history
    }   

    public void removeFlight() {
        this.activePlaneOnGate = null; // Remove plane
        this.isAvailable = true; // Free gate
    }

    public void addPlaneToGateHistory(Flight f) { 
        this.historyPlanesOnGate.add(f);
    }

    // Service Units Management
    public void addUnitToGateHistory(ServiceUnit u) { 
        this.UnitsServedOnGate.add(u);
    } 


    // ************************************************
    //  4. Encapsulation Methods
    // ************************************************
    
    // ========================== Flight Info ==========================
    public int getGateId() { return this.gateId; }
    public boolean getIsAvailable() { return this.isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
    public Flight getActivePlaneOnGate() { return this.activePlaneOnGate; }
    // =================================================================

    // ========================= Lists Getters =========================
    public List<Flight> getPlanesOnGateHistory() { return this.historyPlanesOnGate; }
    public List<ServiceUnit> getUnitsOnGateHistory() { return this.UnitsServedOnGate; }
    // =================================================================


}