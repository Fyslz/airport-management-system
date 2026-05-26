package models;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import interfaces.Serviceable;
import models.Gate;

public abstract class Flight implements Serviceable{
    private String flightId;
    private double flightArrivalTime; // the time when the plane landed
    private String flightArrivalStatus; // did the plane land? - did the plane depart?
    private int priority; 
    private double flightTotalWaitingTime; // Total Waiting time the plane took in Queue and on Gate
    private double flightInQueueWaitingTime; // Waiting time plane took in Queue only
    private double flightOnGateWaitingTime; // Waiting time plane took on gate only // requested units
    private Gate assignedGate; // which gate flight will get on // active units
    private Gate visitedGate; // units histoy

    private List<String> requiredServices = new ArrayList<>(); 
    private List<ServiceUnit> assignedUnits = new ArrayList<>(); 
    private List<ServiceUnit> servedUnitsHistory = new ArrayList<>(); 

    private double readyToDepartTime = -1; // not ready to depart


    public abstract void requestService(String serviceType);

    public Flight(String flightId, double flightArrivalTime, int priority){
        // Assigning inputs
        this.flightId = flightId;
        this.flightArrivalTime = flightArrivalTime;
        this.priority = priority;

        // Initializing default vlaues
        this.flightArrivalStatus = "Scheduled"; // expected to be scheduled 
        this.flightTotalWaitingTime = 0.0; // just got registered 
        this.flightInQueueWaitingTime = 0.0;
        this.flightOnGateWaitingTime = 0.0;
        this.assignedGate = null; // indicates unassigned gate 
        this.visitedGate = null; // indicates unassigned gate 
        this.requiredServices = new ArrayList<>(); // avoid errors
        this.assignedUnits = new ArrayList<>(); // avoid errors
        this.servedUnitsHistory = new ArrayList<>(); // avoid errors
    }


    public void addServiceToList(String serviceType) {
        this.requiredServices.add(serviceType);
    }

    public void addAssignedServiceToList(ServiceUnit serviceUnit) {
        this.assignedUnits.add(serviceUnit);
        this.servedUnitsHistory.add(serviceUnit);
    }

    public void land(Double landingTime) {// landing time is the time when the plane landed (the timeline)
        this.flightArrivalTime = landingTime;
        this.flightArrivalStatus = "Landed";
        System.out.println("Flight [" + flightId + "] has successfully landed and ready for gate assignment.");
    }

    public void depart() {
        this.flightArrivalStatus = "Departed";
        // this.assignedGate.setAvailable(true); // free the gate تم كنسلة هذا اللاين بسبب releaseGate()
        System.out.println("Flight [" + flightId + "] has departed from the airport.");
    }


    public void updateFlightInQueueWaitingTime(){ 
        // Time passes while the flight is in Queue waiting to get assigned to a gate
        this.flightInQueueWaitingTime += 1.0;
    }
    public void updateFlightOnGateWaitingTime(){ 
        // Time passes while the flight is on Gate waiting to get services units to be done
        this.flightOnGateWaitingTime += 1.0;
    }
    public void updateFlightTotalWaitingTime(){
        // Total Time passed to the flight waiting..
        this.flightTotalWaitingTime = this.flightOnGateWaitingTime + this.flightInQueueWaitingTime;
    }


    public void setReadyToDepartTime(double time) { 
        this.readyToDepartTime = time; 
    }
    public double getReadyToDepartTime() { 
        return this.readyToDepartTime; 
    }

    
    public void setAssignedGate(Gate assignedGate){ // Plane is linked to a gate
        this.assignedGate = assignedGate;
        this.visitedGate = assignedGate;
    } 
    public Gate getVisitidGate() {
        return visitedGate;
    }
    public String getFlightId() {return flightId;}
    public double getArrivalTime(){return flightArrivalTime;}
    public String getFlightArrivalStatus() {return flightArrivalStatus;}
    public int getPriority(){return priority;}
    public Gate getAssignedGate() {return this.assignedGate;}
    public int getAssignedGateId(){return this.assignedGate.getGateId();} 
    public double getFlightInQueueWaitingTime() {return flightInQueueWaitingTime;}
    public double getFlightOnGateWaitingTime() {return flightOnGateWaitingTime;}
    public double getFlightTotalWaitingTime() {return flightTotalWaitingTime;}
    public List<ServiceUnit> getAssignedUnits() {return this.assignedUnits;}
    public List<String> getRequestedServices() {return this.requiredServices;}

}
