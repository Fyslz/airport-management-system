package models;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import interfaces.Serviceable;

public abstract class Flight implements Serviceable{

    // ************************************************
    //  1. DATA FIELD / VARIABLES
    // ************************************************

    // Flight information Varaiables
    private String flightId;
    private double flightArrivalTime; // the time when the plane landed
    private String flightArrivalStatus; // did the plane land? - did the plane depart?
    private int priority;

    // Waiting / Timeline Varaiables
    private double flightTotalWaitingTime; // Total Waiting time the plane took in Queue and on Gate
    private double timeEnteredQueue = -1.0; // When flight joined Queue
    private double flightInQueueWaitingTime; // Waiting time plane took in Queue only
    private double timeGateAssigned = -1.0; // When flight Parked on a Gate
    private double flightOnGateWaitingTime; // Waiting time plane took on gate only // requested units
    private double readyToDepartTime = -1; // not ready to depart


    // Gates Variables
    private Gate assignedGate; // which gate flight will get on // active units

    // Services Lists
    private List<String> requiredServices = new ArrayList<>();
    private List<ServiceUnit> assignedUnits = new ArrayList<>();
    private List<String> unServedServicesUnits = new ArrayList<>();
    private Map<ServiceUnit, Double> whenServiceUnitServed = new HashMap<>();


    // ************************************************
    //  2. Constructors
    // ************************************************

    public Flight(String flightId, double flightArrivalTime, int priority){
        // Assigning inputs
        this.flightId = flightId;
        this.flightArrivalTime = flightArrivalTime;
        this.priority = priority;

        // Initializing default vlaues
        this.flightArrivalStatus = "Scheduled"; // to be scheduled 
        this.flightTotalWaitingTime = 0.0; // just got registered 
        this.flightInQueueWaitingTime = 0.0;
        this.flightOnGateWaitingTime = 0.0;
        this.assignedGate = null; // indicates unassigned gate 
    }

    // ************************************************
    //  3. Methods
    // ************************************************

    public abstract void requestService(String serviceType);

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

    public void addServiceToList(String serviceType) {
        this.requiredServices.add(serviceType);
    }

    public void addAssignedServiceToList(ServiceUnit serviceUnit, double timeline) {
        // The service has served
        this.assignedUnits.add(serviceUnit);
        // add history when each service has served
        this.whenServiceUnitServed.put(serviceUnit, timeline);
        // Add service unit to Gate Service Units History
        if (this.assignedGate != null) { // نضيف الخدمة لتاريخ البوابة
            this.assignedGate.addUnitToGateHistory(serviceUnit);
        }
    }

    // Method to update the list of services that had not been provided yet
    public void updateUnServedServices() {
        // Clear the list to avoid duplicated services
        this.unServedServicesUnits.clear();

        // Loop through all required services for the flight
        for (int i = 0; i < this.requiredServices.size(); i++) {
            String reqType = this.requiredServices.get(i);
            boolean isServed = false;

            // Check if this required service has already been assigned
            for (int j = 0; j < this.assignedUnits.size(); j++) {
                if (this.assignedUnits.get(j).getServiceType().equals(reqType)) {
                    isServed = true;
                    break; // Found the service. Leave.
                }
            }

            // If not found, add it to the unserved services list
            if (!isServed) {
                this.unServedServicesUnits.add(reqType);
            }
        }
    }

    // ************************************************
    //  4. Encapsulation Methods
    // ************************************************
    
    // ========================== Flight Info ==========================
    public String getFlightId() { return flightId; }
    public double getArrivalTime() { return flightArrivalTime; }
    public String getFlightArrivalStatus() { return flightArrivalStatus; }
    public int getPriority() { return priority; }
    // =================================================================

    // ======================= GATE Assignments ========================
    public Gate getAssignedGate() { return this.assignedGate; }
    public void setAssignedGate(Gate assignedGate) { this.assignedGate = assignedGate; } 
    public int getAssignedGateId() { return this.assignedGate.getGateId(); } 
    // =================================================================


    // ========================== QUEUE TIMES ==========================
    public void updateFlightInQueueWaitingTime(){ this.flightInQueueWaitingTime += 1.0; } // Time passes while the flight is in Queue waiting to get assigned to a gate
    public double getFlightInQueueWaitingTime() { return this.flightInQueueWaitingTime; }
    public void setTimeEnteredQueue(double timeline) { this.timeEnteredQueue = timeline; }
    public double getTimeEnteredQueue() { return this.timeEnteredQueue; }
    // =================================================================


    // ========================== GATE TIMES ===========================
    public void updateFlightOnGateWaitingTime(){ this.flightOnGateWaitingTime += 1.0; } // Time passes while the flight is on Gate waiting to get services units to be done
    public double getFlightOnGateWaitingTime() { return this.flightOnGateWaitingTime; }
    public void setTimeGateAssigned(double timeline) { this.timeGateAssigned = timeline; }
    public double getTimeGateAssigned() { return this.timeGateAssigned; }
    // =================================================================


    // ====================== TOTAL WAITING TIMES =======================
    // Total Time passed to the flight waiting..
    public void updateFlightTotalWaitingTime(){ this.flightTotalWaitingTime = this.flightOnGateWaitingTime + this.flightInQueueWaitingTime; }
    public double getFlightTotalWaitingTime() { return this.flightTotalWaitingTime; }
    // ==================================================================

    
    // ======================== Services Lists =========================
    public List<String> getRequestedServices() { return this.requiredServices;}
    public List<ServiceUnit> getAssignedUnits() { return this.assignedUnits; }
    public Map<ServiceUnit, Double> getWhenServiceUnitServed() { return whenServiceUnitServed; }
    public List<String> getUnServedServicesUnits() { updateUnServedServices(); return unServedServicesUnits; }
    // =================================================================

    public void setReadyToDepartTime(double time) { this.readyToDepartTime = time; }
    public double getReadyToDepartTime() { return this.readyToDepartTime; }
}
