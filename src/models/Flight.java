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
    private List<String> requiredServices;

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

    public void land(){
        // to be assigned later
    }

    public void depart(){
        // to be assigned later
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

    // -------------------------------------
    // Additional Getters (Added by Fysl)
    // -------------------------------------

    public String getFlightId() {
        return flightId;
    }

    public int getAssignedGate() {
        return assignedGate;
    }

}
