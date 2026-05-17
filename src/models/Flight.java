package models;
import java.util.ArrayList;
import java.util.List;
import interfaces.Serviceable;

public abstract class Flight implements Serviceable{
    private String flightId;
    private double flightArrivalTime;
    private String flightArrivalStatus;
    private int priority; 
    private double waitingTime;
    private int assignedGate;
    private List<String> requiredServices; // requested units
    private List<ServiceUnit> assignedUnits = new ArrayList<>(); // active units


    public abstract void requestService(String serviceType);

    public Flight(String flightId, double flightArrivalTime, int priority){
    // Assigning inputs
    this.flightId = flightId;
    this.flightArrivalTime = flightArrivalTime;
    this.priority = priority;

    // Initializing default vlaues
    this.flightArrivalStatus = "Scheduled"; // expected to be scheduled 
    this.waitingTime = 0.0; // just got registered 
    this.assignedGate = -1; // indicates unassigned gate
    this.requiredServices = new ArrayList<>(); // avoid errors
    }

    public void land() {
        this.flightArrivalStatus = "Landed";
        System.out.println("Flight [" + flightId + "] has successfully landed and ready for gate assignment.");
    }

    public void depart() {
        this.flightArrivalStatus = "Departed";
        this.assignedGate = -1; 
        System.out.println("Flight [" + flightId + "] has departed from the airport.");
    }
    
    public void addServiceToList(String serviceType) {
        this.requiredServices.add(serviceType);
    }

    public void setAssignedGate(int assignedGate){
        this.assignedGate = assignedGate;
    }

    public int getPriority(){
        return priority;
    }

    public double getWaitingTime(){
        return waitingTime;
    }

    public double getArrivalTime(){
        return flightArrivalTime;
    }

    public String getFlightArrivalStatus() {
        return flightArrivalStatus;
    }

    public String getFlightId() {
        return flightId;
    }

    public int getAssignedGate() {
        return assignedGate;
    }
    
    public List<String> getRequestedServices() {
        return this.requiredServices;
    }
    
    public List<ServiceUnit> getAssignedUnits() {
        return this.assignedUnits;
    }
}
