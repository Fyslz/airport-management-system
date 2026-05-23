package models;

import java.util.ArrayList;
import java.util.List;

public class Gate {
    private int gateId;
    private boolean isAvailable;
    private List<ServiceUnit> unitsServedOnGate = new ArrayList<>(); // List of units that has been used on this gate
    private List<Flight> planeStoppedOnGate = new ArrayList<>(); // List of planes that parked on this gate

    public Gate(int gateId){
        this.gateId = gateId; // gate number
        this.isAvailable = true; // free by default 
        this.unitsServedOnGate = new ArrayList<>(); // avoiding unexpected errors
        this.planeStoppedOnGate = new ArrayList<>();
    }

    public void addUnitToGateHistory(ServiceUnit u){ // add unit who served on the gate
        this.unitsServedOnGate.add(u);
    }

    public void addPlaneToGateHistory(Flight f){ // add plane who parked on the gate
        this.planeStoppedOnGate.add(f);
    }
    public Flight getActivePlaneOnGate(){ // which plane is on the gate
        int lastIndex = this.planeStoppedOnGate.size() - 1;
        return planeStoppedOnGate.get(lastIndex);
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public int getGateId() {
        return gateId;
    }
    public boolean getIsAvailable(){
        return this.isAvailable;
    }
    public List<Flight> getPlaneStoppedOnGate() {
        return planeStoppedOnGate;
    }
    public List<ServiceUnit> getUnitsServedOnGate() {
        return unitsServedOnGate;
    }

}
