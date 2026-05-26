package models;

import java.util.ArrayList;
import java.util.List;

public class Gate {
    private int gateId;
    private boolean isAvailable;
    private Flight activePlaneOnGate; // List of planes that parked on this gate


    private List<ServiceUnit> historyUnitsServedOnGate = new ArrayList<>(); // List of units that has been used on this gate
    private List<ServiceUnit> activeUnitsServedOnGate = new ArrayList<>(); // List of units that has been used on this gate
    private List<Flight> historyPlanesOnGate = new ArrayList<>(); // List of planes that parked on this gate

    public Gate(int gateId){
        this.gateId = gateId; // gate number    
        this.isAvailable = true; // free by default 
        this.activePlaneOnGate = null;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void addUnitToGateHistory(ServiceUnit u){ // add unit who served on the gate
        this.historyUnitsServedOnGate.add(u);
    } 

    public void addActiveUnitToGate(ServiceUnit u){ // add unit who are serving on the gate
        this.activeUnitsServedOnGate.add(u);
        addUnitToGateHistory(u); // also add in history
    }
    
    public void addPlaneToGateHistory(Flight f){ // add plane who parked on the gate
        this.historyPlanesOnGate.add(f);
    }

    public void addPlaneToGate(Flight f) {
        this.activePlaneOnGate = f; // park plane to gate
        setAvailable(false); // close gate
        this.addPlaneToGateHistory(f); // add plane to history
    }   

    public void removeFlight() {
        this.activePlaneOnGate = null; // remove plane
        this.isAvailable = true; // free gate
    }

    public Flight getActivePlaneOnGate() {return this.activePlaneOnGate;} // which plane is on the gate
    public int getGateId() {return this.gateId;}
    public boolean getIsAvailable(){return this.isAvailable;}
    public List<Flight> getPlanesOnGateHistory() {return this.historyPlanesOnGate;}
    public List<ServiceUnit> getActiveUnitsServedOnGate() {return activeUnitsServedOnGate;}
    public List<ServiceUnit> getUnitsOnGateHistory() {return this.historyUnitsServedOnGate;}

}
