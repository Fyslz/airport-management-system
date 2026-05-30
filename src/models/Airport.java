package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import services.*;

public class Airport {

    // ************************************************
    //  1. DATA FIELDS / VARIABLES
    // ************************************************
    
    // -- The services the airport provide --
    private static final String[] ALL_SERVICES = {
        "Ambulance", "FuelTruck", "CleaningCrew", "BaggageHandler", 
        "CateringTruck", "Stairs", "PassengerBus", "PushbackTug", 
        "FireTruck", "MaintenanceService", "CustomsService", "SpecialAssistance"
    };

    // -- Lists & Queues --
    private List<Flight> flights;  // Flights List
    private List<ServiceUnit> serviceUnits; // Available Service Units
    private Queue<Flight> waitingQueue; // Planes Waiting for a gate to get free Queue
    private List<Gate> airportGates = new ArrayList<>(); // Gates

    // -- Airport Statistics & Timeline --
    private double totalWaitingTime;
    private double averageWaitTime;
    private double totalCost;
    private double timeLine;


    // ************************************************
    //  2. Constructor
    // ************************************************
    
    public Airport(int numberOfGates) {
        this.flights = new ArrayList<>();
        this.serviceUnits = new ArrayList<>();
        this.waitingQueue = new LinkedList<>();

        // Initialize and create gates
        for (int i = 0; i < numberOfGates; i++) { 
            this.airportGates.add(new Gate(i));
        }

        // Initialize default statistics
        this.totalWaitingTime = 0.0;
        this.totalCost = 0.0;
        this.timeLine = 0.0;
    }


    // ************************************************
    //  3. Methods
    // ************************************************
    
    // ================== Airport Management =================
    public void generateFlights(){
    System.out.println("\n--- Initializing Service Units ---");
        addServiceUnit(new Ambulance(101));         addServiceUnit(new Ambulance(102));
        addServiceUnit(new FuelTruck(201));         addServiceUnit(new FuelTruck(202));
        addServiceUnit(new CleaningCrew(301));      addServiceUnit(new CleaningCrew(302));
        addServiceUnit(new BaggageHandler(401));    addServiceUnit(new BaggageHandler(402));
        addServiceUnit(new CateringTruck(501));     addServiceUnit(new CateringTruck(502));
        addServiceUnit(new Stairs(601));            addServiceUnit(new Stairs(602));
        addServiceUnit(new PassengerBus(701));      addServiceUnit(new PassengerBus(702));
        addServiceUnit(new PushbackTug(801));       addServiceUnit(new PushbackTug(802));
        addServiceUnit(new FireTruck(901));         addServiceUnit(new FireTruck(902));
        addServiceUnit(new MaintenanceService(1001)); addServiceUnit(new MaintenanceService(1002));
        addServiceUnit(new CustomsService(1101));   addServiceUnit(new CustomsService(1102));
        addServiceUnit(new SpecialAssistance(1201));  addServiceUnit(new SpecialAssistance(1202));

        System.out.println("--- Generating 10 Flights ---");
        Flight[] incomingFlights = new Flight[10];
        
        incomingFlights[0] = new PassengerPlane("SV111", 1.0, 1, 150);
        incomingFlights[1] = new CargoPlane("XY222", 2.0, 2, 2000.0);
        incomingFlights[2] = new PassengerPlane("EK333", 3.0, 1, 300);
        incomingFlights[3] = new CargoPlane("QR444", 4.0, 1, 1500.0);
        incomingFlights[4] = new PassengerPlane("GF555", 5.0, 2, 180);
        incomingFlights[5] = new PassengerPlane("WY666", 6.0, 3, 220);
        incomingFlights[6] = new CargoPlane("MS777", 7.0, 1, 3500.0);
        incomingFlights[7] = new PassengerPlane("TK888", 8.0, 2, 250);
        incomingFlights[8] = new PassengerPlane("BA999", 9.0, 1, 280);
        incomingFlights[9] = new CargoPlane("LH100", 10.0, 1, 4000.0);

        System.out.println("--- Assigning Services & Receiving Flights ---");
        for (int i = 0; i < 10; i++) {
            assignRandomServices(incomingFlights[i]);
            
            receiveFlight(incomingFlights[i]);
        }
    }

    public void run(){
        System.out.println("=== AIRPORT SIMULATION STARTED ===");
        generateFlights(); // generate the flights and service unis
        
        int totalFlights = this.flights.size();
        int departedFlightsCount = 0;

        System.out.println("\n--- Processing Airport Dynamics (TimeLine Simulation) ---");
        
        while (departedFlightsCount < totalFlights) { // end when every flight is departed

            this.timeLine += 1.0; // time is passing..
            
            // update waiting time for each plane on airport
            for (int i = 0; i < this.flights.size(); i++) {
                Flight f = this.flights.get(i);
                
                if (f.getAssignedGate() == null && f.getFlightArrivalStatus().equals("Landed")) { 
                    // planes in Queue and wasn't assigned to gate
                    f.updateFlightInQueueWaitingTime();
                } else if (f.getAssignedGate() != null && !f.getFlightArrivalStatus().equals("Departed")) {
                    // planes on gated and getting services or waiting for services
                    f.updateFlightOnGateWaitingTime(); 
                }
            }
            
            // give every plane on gate its services (Priority System)
            assignServicesByPriority();

            // re-check which plane did not get all services
            for (int i = 0; i < this.flights.size(); i++) {
                Flight f = this.flights.get(i);
                
                if (f.getAssignedGate() != null && !f.getFlightArrivalStatus().equals("Departed")) { // if plane has gate and not "Departed"
                    
                    if (f.getAssignedUnits().size() == f.getRequestedServices().size() && f.getRequestedServices().size() > 0) {
                        // plane finished got all services                        
                        
                        if (f.getReadyToDepartTime() == -1) {
                            System.out.println("\nFlight [" + f.getFlightId() + "] received all requested services. Departing...");

                            // ready to depart in 15 minutes..
                            f.setReadyToDepartTime(this.timeLine + 15.0); 
                            System.out.println("[Time: " + this.timeLine + "] Flight [" + f.getFlightId() + "] received all services. Will depart at minute: " + f.getReadyToDepartTime());
                        } 
                        
                        else if (this.timeLine >= f.getReadyToDepartTime()) { // is timeline == to plane depart time?
                            System.out.println("\n[Time: " + this.timeLine + "] Flight [" + f.getFlightId() + "] is DEPARTING now!");
                            
                            // freeing plane's services
                            List<ServiceUnit> units = f.getAssignedUnits(); 
                            for (int k = 0; k < units.size(); k++) {
                                units.get(k).setAvailable(true);
                                System.out.println("   -> Service [" + units.get(k).getServiceType() + " ID: " + units.get(k).getUnitId() + "] is now FREE.");
                            }
                        
                            f.updateFlightTotalWaitingTime(); // update waiting time (Queue + on gate) waiting time

                            // plane is leaving the gate..
                            dispatchFlight(f);
                            departedFlightsCount++;
                        System.out.println("--------------------------------------------------");
                        }
                    }
                }
            }

        }
        
        System.out.println("=== ALL FLIGHTS DEPARTED AT MINUTE: " + this.timeLine + " ===");
        printResults();
    }

    public void printResults() {
        // ------------------------ Calculations ------------------------
        calculateWaitingTime();
        calculateTotalCost();
        // ------------------------ Calculations ------------------------

        System.out.println("\n=========================================================");
        System.out.println("                   FINAL AIRPORT REPORT                   ");
        System.out.println("=========================================================");
        
        System.out.println("SIMULATION SUMMARY:");
        System.out.println("    - Total Simulation Time : " + this.timeLine + " minutes");
        System.out.println("    - Total Flights Handled : " + this.flights.size() + " flights");
        
        System.out.println("\nWAITING TIME STATISTICS:");
        System.out.println("    - Total Waiting Time    : " + this.totalWaitingTime + " minutes");
        System.out.println("    - Average Wait / Flight : " + String.format("%.2f", this.averageWaitTime) + " minutes");
        
        System.out.println("\nFINANCIAL & RESOURCES:");
        System.out.println("    - Total Daily Cost      : $" + this.totalCost);
        System.out.println("    - Total Service Units   : " + this.serviceUnits.size() + " units");
        System.out.println("    - Available Units Now   : " + ServiceUnit.getAvailableServiceUnits() + " units");
        
        System.out.println("=========================================================");
        System.out.println("         End of Operations - Simulation Complete         ");
        System.out.println("=========================================================\n");
    }

    // ================== Flight Management ==================

    public void receiveFlight(Flight flight) {
        this.flights.add(flight);
        flight.land(this.timeLine);
        assignFlightToGate(flight);
    }

    public void dispatchFlight(Flight flight) {
        Gate gateToRelease = flight.getAssignedGate(); 
        flight.depart(); 
        
        if (gateToRelease != null) {
            releaseGate(gateToRelease);
        }
    }

    // ================== Gate Management ====================

    public void assignFlightToGate(Flight flight) {
        for (Gate gate : this.airportGates) {
            if (gate.getIsAvailable()) {
                gate.addPlaneToGate(flight);
                flight.setAssignedGate(gate);
                System.out.println("Flight: " + flight.getFlightId() + " to the gate: " + flight.getAssignedGateId());
                return;
            }
        }
        
        // If no gates are available, add to the waiting queue
        this.waitingQueue.add(flight);
        System.out.println("Flight: " + flight.getFlightId() + " is waiting for a free gate...");
        // TODO: Add waiting time tracking here
    }

    public void releaseGate(Gate g) {
        g.removeFlight();
        System.out.println("Gate: " + g.getGateId() + " is free now!");

        // If there are flights waiting, assign the first one in queue to this newly freed gate
        if (!this.waitingQueue.isEmpty()) { 
            Flight nextFlight = this.waitingQueue.poll(); 
            System.out.println("Ready to serve flight " + nextFlight.getFlightId());
            assignFlightToGate(nextFlight);
        }
    }

    // ================= Service Management ==================

    public void addServiceUnit(ServiceUnit unit) {
        this.serviceUnits.add(unit);
        // System.out.println("Added new service unit: " + unit.getServiceType() + " ID: " + unit.getUnitId());
    }

    public void assignRandomServices(Flight flight) {
        List<String> shuffledServices = new ArrayList<>(Arrays.asList(ALL_SERVICES));
        Collections.shuffle(shuffledServices);
        
        Random rand = new Random();
        int numberOfServicesToRequest = rand.nextInt(ALL_SERVICES.length) + 1; 
        
        for (int i = 0; i < numberOfServicesToRequest; i++) {
            flight.requestService(shuffledServices.get(i));
        }
    }

    public void assignServicesByPriority() {
        for (ServiceUnit currentUnit : this.serviceUnits) {
            
            if (currentUnit.isAvailable()) {
                Flight highestPriorityFlight = null;
                double maxWaitTime = -1.0;

                // Find the most delayed flight that needs this specific service
                for (Flight f : this.flights) {
                    if (f.getAssignedGate() != null && !f.getFlightArrivalStatus().equals("Departed")) {
                        
                        // Check if the flight requested this service type
                        boolean needsThisService = f.getRequestedServices().contains(currentUnit.getServiceType());
                        boolean alreadyGotIt = false;
                        
                        for (ServiceUnit assignedUnit : f.getAssignedUnits()) {
                            if (assignedUnit.getServiceType().equals(currentUnit.getServiceType())) {
                                alreadyGotIt = true;
                                break;
                            }
                        }

                        // If the flight needs the service and hasn't received it yet
                        if (needsThisService && !alreadyGotIt) {
                            double currentWait = f.getFlightInQueueWaitingTime() + f.getFlightOnGateWaitingTime(); 
                            
                            if (currentWait > maxWaitTime) {
                                highestPriorityFlight = f;
                                maxWaitTime = currentWait;
                            }
                        }
                    }
                }

                // Dispatch the unit to the highest priority (most delayed) flight found
                if (highestPriorityFlight != null) {
                    // System.out.println("Found available: " + currentUnit.getServiceType() + "!, Dispatching to most delayed flight: " + highestPriorityFlight.getFlightId());
                    currentUnit.provideService(highestPriorityFlight);
                    highestPriorityFlight.addAssignedServiceToList(currentUnit, this.timeLine); // Note: Assuming timeline is passed based on your previous code structure
                }
            }
        }
    }

    // Dead method To be deleted
    public void assignServiceUnit(Flight flight, String serviceType) {
        for (ServiceUnit currentUnit : this.serviceUnits) {
            if (currentUnit.getServiceType().equals(serviceType) && currentUnit.isAvailable()) { 
                System.out.println("Found available: " + serviceType + "!, Dispatching Unit: " + currentUnit.getUnitId() + " for flight: " + flight.getFlightId());
                
                currentUnit.provideService(flight);
                flight.addAssignedServiceToList(currentUnit, this.timeLine);
                return;  // Leave so we don't add similar services
            }
        }
        // System.out.println("Service [" + serviceType + "] is BUSY! Flight [" + flight.getFlightId() + "] is waiting at gate " + flight.getAssignedGateId());
    }

    // ==================== Calculations =====================

    public void calculateWaitingTime() {
        this.totalWaitingTime = 0.0; // Reset total
        
        for (Flight f : this.flights) {
            this.totalWaitingTime += f.getFlightTotalWaitingTime();
        }

        if (!this.flights.isEmpty()) {
            this.averageWaitTime = this.totalWaitingTime / this.flights.size();
        }
    }


    // Method to calculate the total cost of ALL service units (Fleet Cost)
    public double calculateTotalCost() {
        double fleetCost = 0.0; 

        for (int i = 0; i < this.serviceUnits.size(); i++) {
            ServiceUnit currentUnit = this.serviceUnits.get(i);
            fleetCost += currentUnit.getCost(); 
        }
        
        return fleetCost;
    }
    
    // Method to calculate the cost of only the USED service units (Operational Cost)
    public double calculateOperationalCost() {
        double currentOperationalCost = 0.0;

        for (int i = 0; i < this.serviceUnits.size(); i++) {
            ServiceUnit currentUnit = this.serviceUnits.get(i);
            
            // Check if the unit has served any planes today
            if (!currentUnit.getPlanesServedHistory().isEmpty()) { 
                currentOperationalCost += currentUnit.getCost();
            } 
        }
        
        return currentOperationalCost;
    }
    

    // Method to get total usage count for a specific service type
    public int getTotalUsageOfService(String serviceType) {
        int totalUsage = 0;

        for (int i = 0; i < this.serviceUnits.size(); i++) {
            ServiceUnit unit = this.serviceUnits.get(i);

            // Check if the unit matches the requested service type
            if (unit.getServiceType().equals(serviceType)) {
                // Add the number of times this specific unit was used
                totalUsage += unit.getPlanesServedHistory().size();
            }
        }

        return totalUsage;
    }

    
    // Method to get the operational cost for a specific service type (Used units only)
    public double getOperationalCostOfServiceType(String serviceType) {
        double typeOperationalCost = 0.0;

        for (int i = 0; i < this.serviceUnits.size(); i++) {
            ServiceUnit unit = this.serviceUnits.get(i);
            
            // إذا السيارة من نفس النوع اللي نبيه + اشتغلت اليوم (تاريخها مو فاضي)
            if (unit.getServiceType().equals(serviceType) && !unit.getPlanesServedHistory().isEmpty()) {
                typeOperationalCost += unit.getCost(); // نجمع إيجارها اليومي مرة وحدة
            }
        }

        return typeOperationalCost;
    }

    // Method to calculate the total cost of ALL units for a specific service type
    public double getTotalCostOfServiceType(String serviceType) {
        double typeTotalCost = 0.0;

        for (int i = 0; i < this.serviceUnits.size(); i++) {
            ServiceUnit unit = this.serviceUnits.get(i);
            
            // If the unit matches the requested type, add its cost
            if (unit.getServiceType().equals(serviceType)) {
                typeTotalCost += unit.getCost();
            }
        }

        return typeTotalCost;
    }

    // ************************************************
    //  4. Encapsulation Methods
    // ************************************************

    public double getTotalCost() {
        return this.totalCost;
    }
    
    public double getCurrentTime() {
        return this.timeLine;
    }
    
    public double getAverageWaitTime() {
        return this.averageWaitTime;
    }

}