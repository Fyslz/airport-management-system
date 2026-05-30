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
    
    // ======================= Airport Management ======================
private void generateServices() {
        System.out.println("\n--- Initializing Service Units (SCENARIO 5 - EVIL) ---");
        this.serviceUnits.clear(); 
        
        // المطار طفران، ما فيه إلا درج واحد فقط!
        addServiceUnit(new Stairs(601)); 
    }

    private void generateFlights() {
        System.out.println("--- Generating 1 Cursed Flight ---");
        this.flights.clear();

        Flight cursedFlight = new PassengerPlane("DOOM-666", 1.0, 1, 150);
        
        // الطيارة تطلب الدرج (موجود وبتاخذه)
        cursedFlight.requestService("Stairs"); 
        
        // 🚨 الطلب الخبيث: تطلب بنزين، والمطار ما عنده ولا سيارة بنزين!
        cursedFlight.requestService("FuelTruck"); 

        System.out.println("--- Receiving Cursed Flight ---");
        receiveFlight(cursedFlight);
    }
    
    public void run() {
        System.out.println("=== AIRPORT SIMULATION STARTED ===");
        generateServices();
        generateFlights(); 
        
        int totalFlights = this.flights.size();
        int departedFlightsCount = 0;

        System.out.println("\n--- Processing Airport Dynamics (TimeLine Simulation) ---");
        
        // Main Simulation Loop
        while (departedFlightsCount < totalFlights) { 
            
            this.timeLine += 1.0; // Time ticks forward..
            
            // ---------------------------------------------------------
            // أمر طباعة لتتبع مرور الوقت في المحاكاة
            System.out.println("\n[ >>> Minute: " + this.timeLine + " <<< ]");
            // ---------------------------------------------------------

            updateActiveFlightsWaitingTimes();
            assignServiceUnit(); 
            processReadyDepartures();
            
            // تحديث العداد بناءً على عدد الطيارات اللي غادرت فعلاً
            departedFlightsCount = getDepartedFlightsCount();

            // ---------------------------------------------------------
            // إبطاء التنفيذ لمدة ثانية واحدة (1000 ملي ثانية) لتقرأ المخرجات
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // ---------------------------------------------------------
        }
        
        System.out.println("\n=== ALL FLIGHTS DEPARTED AT MINUTE: " + this.timeLine + " ===");
        printResults();
    }
    
    // دالة مساعدة لحساب الطيارات المغادرة وتحديث الـ Loop
    private int getDepartedFlightsCount() {
        int count = 0;
        for (Flight f : this.flights) {
            if (f.getFlightArrivalStatus().equals("Departed")) {
                count++;
            }
        }
        return count;
    }


    // ======================== Display Results ========================
    // Flight Summary Report
    public void printSingleFlightSummary(Flight f) {
        if (f == null) {
            System.out.println("Error: Provided flight is null.");
            return;
        }

        System.out.println("Flight ID: [" + f.getFlightId() + "] | Status: " + f.getFlightArrivalStatus());
        System.out.println("---------------------------------------------------------");

        // 1. Queue Information
        if (f.getTimeEnteredQueue() != -1.0) {
            System.out.println("  [Queue] Entered Queue at Minute : " + f.getTimeEnteredQueue());
            System.out.println("  [Queue] Time Waited in Queue    : " + f.getFlightInQueueWaitingTime() + " mins");
        } else {
            System.out.println("  [Queue] Status                  : Direct to Gate (No Queue)");
        }

        // 2. Gate Information
        if (f.getAssignedGate() != null || f.getFlightArrivalStatus().equals("Departed")) {
            System.out.println("  [Gate]  Assigned Gate ID        : " + f.getAssignedGateId());
            System.out.println("  [Gate]  Parked at Minute        : " + (f.getTimeGateAssigned() != -1.0 ? f.getTimeGateAssigned() : "N/A"));
            System.out.println("  [Gate]  Time Waited on Gate     : " + f.getFlightOnGateWaitingTime() + " mins");
        } else {
            System.out.println("  [Gate]  Status                  : Still waiting in Queue...");
        }

        // 3. Services Information
        System.out.println("  [Services] Requested: " + f.getRequestedServices().size() + " | Received: " + f.getAssignedUnits().size());
        if (!f.getAssignedUnits().isEmpty()) {
            for (ServiceUnit unit : f.getAssignedUnits()) {
                double serveTime = f.getWhenServiceUnitServed().getOrDefault(unit, -1.0);
                System.out.println("      -> Received: " + unit.getServiceType() + " [ID: " + unit.getUnitId() + "] at Minute: " + serveTime);
            }
        }

        // 4. Extra Information
        if (f instanceof CargoPlane) {
            System.out.println("  [Extra] Cargo Weight            : " + ((CargoPlane) f).getCargoWeight() + " kg");
        } else if (f instanceof PassengerPlane) {
            System.out.println("  [Extra] Passenger Count         : " + ((PassengerPlane) f).getPassengerCount() + " passengers");
        }
        
        System.out.println("  [Total] Total Wait Time         : " + f.getFlightTotalWaitingTime() + " mins");
        System.out.println("=========================================================");
    }

    // All Flights Summary Report
    public void printAllFlightsSummary() {
        System.out.println("\n\n=========================================================");
        System.out.println("                DETAILED FLIGHTS TRACKING LOG            ");
        System.out.println("=========================================================");

        for (Flight f : this.flights) {
            printSingleFlightSummary(f);
        }
    }

    public void printResults() {
        // ------------------------ Calculations ------------------------
        calculateAvgWaitingTime();
        double fleetCost = calculateTotalCost();
        double operationalCost = calculateOperationalCost();
        // ------------------------ Calculations ------------------------

        System.out.println("\n=========================================================");
        System.out.println("                  FINAL AIRPORT REPORT                   ");
        System.out.println("=========================================================");
        
        System.out.println("1. SIMULATION SUMMARY:");
        System.out.println("   - Total Simulation Time   : " + this.timeLine + " minutes");
        System.out.println("   - Total Flights Handled   : " + this.flights.size() + " flights");
        
        System.out.println("\n2. WAITING TIME STATISTICS:");
        System.out.println("   - Total Waiting Time      : " + this.totalWaitingTime + " minutes");
        System.out.println("   - Average Wait / Flight   : " + String.format("%.2f", this.averageWaitTime) + " minutes");
        
        System.out.println("\n3. FINANCIAL REPORT:");
        System.out.println("   - Total Fleet Capacity    : $" + String.format("%.2f", fleetCost));
        System.out.println("   - Actual Operational Cost : $" + String.format("%.2f", operationalCost));
        System.out.println("   - Unused Resource Value   : $" + String.format("%.2f", (fleetCost - operationalCost)));
        
        System.out.println("\n4. RESOURCES STATUS:");
        System.out.println("   - Total Service Units     : " + this.serviceUnits.size() + " units");
        System.out.println("   - Currently Available     : " + ServiceUnit.getAvailableServiceUnits() + " units");
        
        System.out.println("=========================================================");
        System.out.println("         End of Operations - Simulation Complete         ");
        System.out.println("=========================================================\n");
    }

    // Free units and gate when plane is done
    private void processReadyDepartures() {

        for (int i = 0; i < this.flights.size(); i++) {
            Flight f = this.flights.get(i);
            
            // Skip flights on Queue or departed already
            if (f.getAssignedGate() == null || f.getFlightArrivalStatus().equals("Departed")) {
                continue;
            }
            
            // Check if the flight has received all its services
            if (f.getAssignedUnits().size() == f.getRequestedServices().size()) {
                
                System.out.println("\n[Time: " + this.timeLine + "] Flight [" + f.getFlightId() + "] received all services. DEPARTING now!");
                
                // Freeing all service units attached to this flight
                List<ServiceUnit> units = f.getAssignedUnits(); 
                for (int k = 0; k < units.size(); k++) {
                    units.get(k).setAvailable(true);
                    System.out.println("   -> Service [" + units.get(k).getServiceType() + " ID: " + units.get(k).getUnitId() + "] is now FREE.");
                }
            
                f.updateFlightTotalWaitingTime(); // Find Total Waiting time for this plane
                dispatchFlight(f); // Dispatch flight and free gate
                System.out.println("--------------------------------------------------");
            }
        }
    }

    // ======================= Flight Management =======================

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

    // ======================= Gate Management =========================

    public void assignFlightToGate(Flight flight) {
        for (Gate gate : this.airportGates) {
            if (gate.getIsAvailable()) {
                gate.addPlaneToGate(flight);
                flight.setAssignedGate(gate);
                flight.setTimeGateAssigned(this.timeLine);
                System.out.println("Flight: " + flight.getFlightId() + " to the gate: " + flight.getAssignedGateId());
                return;
            }
        }
        
        // If no gates are available, add to the waiting queue
        this.waitingQueue.add(flight);
        flight.setTimeGateAssigned(this.timeLine);
        System.out.println("Flight: " + flight.getFlightId() + " is waiting for a free gate...");
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

    // ====================== Service Management =======================

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

    // assign service to most waited flight
    public void assignServiceUnit() {
        for (ServiceUnit currentUnit : this.serviceUnits) {
            // Check for Available services only
            if (!currentUnit.isAvailable()) {
                continue; 
            }
            
            // Find most delayed plane that is waiting for service
            Flight targetFlight = findMostDelayedFlightForService(currentUnit);

            // Plane is founded! Assign service to plane..
            if (targetFlight != null) { // Avoid unexpected errors
                currentUnit.provideService(targetFlight);
                targetFlight.addAssignedServiceToList(currentUnit, this.timeLine); 
            }
        }
    }

    // ============================= Time ==============================
    
    // Update the waiting time for all flights
    private void updateActiveFlightsWaitingTimes() {
        for (int i = 0; i < this.flights.size(); i++) {
            Flight f = this.flights.get(i);
            
            // If plane is waiting in Queue
            if (f.getAssignedGate() == null && f.getFlightArrivalStatus().equals("Landed")) { 
                f.updateFlightInQueueWaitingTime();
            } 
            // If plane is parked on a Gate and waiting for services
            else if (f.getAssignedGate() != null && !f.getFlightArrivalStatus().equals("Departed")) {
                f.updateFlightOnGateWaitingTime(); 
            }
        }
    }
    // Get most waited plane that is still waiting for a service
    private Flight findMostDelayedFlightForService(ServiceUnit unit) {
        Flight highestPriorityFlight = null;
        double maxWaitTime = -1.0;

        for (Flight f : this.flights) {
            
            // skip this flight if departing or has no gate
            if (f.getAssignedGate() == null || f.getFlightArrivalStatus().equals("Departed")) {
                continue; 
            }

            // Is service unserved?
            if (f.getUnServedServicesUnits().contains(unit.getServiceType())) {
                
                double currentWait = f.getFlightTotalWaitingTime(); 
                // Compare Time
                if (currentWait > maxWaitTime) {
                    highestPriorityFlight = f;
                    maxWaitTime = currentWait;
                }
            }
        }
        // Return most waited plane
        return highestPriorityFlight;
    }
    // Average waiting time for a plane in airport
    public void calculateAvgWaitingTime() {
        this.totalWaitingTime = 0.0; // Reset total
        
        for (Flight f : this.flights) {
            this.totalWaitingTime += f.getFlightTotalWaitingTime();
        }

        if (!this.flights.isEmpty()) {
            this.averageWaitTime = this.totalWaitingTime / this.flights.size();
        }
    }

    // ======================= Cost Calculations =======================

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