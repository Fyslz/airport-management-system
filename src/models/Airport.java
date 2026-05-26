package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import models.Flight;
import services.Ambulance;
import services.BaggageHandler;
import services.CateringTruck;
import services.CleaningCrew;
import services.CustomsService;
import services.FireTruck;
import services.FuelTruck;
import services.MaintenanceService;
import services.PassengerBus;
import services.PushbackTug;
import services.SpecialAssistance;
import services.Stairs;
// import services.*;

public class Airport {
    private List<Flight> flights; 
    private List<ServiceUnit> serviceUnits;
    private Queue<Flight> waitingQueue;
    private double totalWaitingTime;
    private double averageWaitTime;
    private double totalCost;
    private double timeLine;
    private List<Gate> airportGates = new ArrayList<>();

    private static final String[] ALL_SERVICES = {
        "Ambulance", "FuelTruck", "CleaningCrew", "BaggageHandler", 
        "CateringTruck", "Stairs", "PassengerBus", "PushbackTug", 
        "FireTruck", "MaintenanceService", "CustomsService", "SpecialAssistance"
    };

    public Airport(int numberOfGates){
        this.flights = new ArrayList<>();
        this.serviceUnits = new ArrayList<>();
        this.waitingQueue = new LinkedList<>();

        for (int i = 0; i < numberOfGates; i++) { // creating gates
            this.airportGates.add(new Gate(i));
        }

        this.totalWaitingTime = 0.0;
        this.totalCost = 0.0;
        this.timeLine = 0.0;

    }

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
            
            for (int i = 0; i < this.flights.size(); i++) { // give every plane that already on gate its services
                Flight f = this.flights.get(i); 
                
                if (f.getAssignedGate() != null && !f.getFlightArrivalStatus().equals("Departed")) { // if plane has gate and not "Departed"
                    List<String> currentRequests = f.getRequestedServices(); // requested services of plane (i)

                    for (int j = 0; j < currentRequests.size(); j++) {
                        String reqType = currentRequests.get(j);
                        
                        // check service is not already on duty
                        boolean alreadyAssigned = false;
                        for (int k = 0; k < f.getAssignedUnits().size(); k++) {
                            if (f.getAssignedUnits().get(k).getServiceType().equals(reqType)) {
                                alreadyAssigned = true;
                                break; // service is already serving the planem, go for next service
                            }
                        }
                        
                        if (!alreadyAssigned) { // give service 
                            assignServiceUnit(f, reqType);
                        }
                    }
                }
            }

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

    public void assignRandomServices(Flight flight) {
        List<String> shuffledServices = new ArrayList<>(Arrays.asList(ALL_SERVICES));
        Collections.shuffle(shuffledServices);
        Random rand = new Random();
        int numberOfServicesToRequest = rand.nextInt(ALL_SERVICES.length) + 1; 
        
        for (int i = 0; i < numberOfServicesToRequest; i++) {
            flight.requestService(shuffledServices.get(i));
        }
    }

    public void assignFlightToGate(Flight flight){
        for (int i = 0; i < this.airportGates.size(); i++){
            Gate gate = this.airportGates.get(i);
            if (this.airportGates.get(i).getIsAvailable()){
                gate.addPlaneToGate(flight);
                flight.setAssignedGate(this.airportGates.get(i));
                System.out.println("Flight: " + flight.getFlightId() + " to the gate: " + flight.getAssignedGateId());
                return;
            }
        }
        waitingQueue.add(flight);
        System.out.println("Flight: " + flight.getFlightId() + " is waiting for a free gate...");
        // TODO: Add waiting time here
    }
    
    public void assignServiceUnit(Flight flight, String serviceType){

        for (int i = 0; i < serviceUnits.size(); i++) {
            ServiceUnit currentUnit = serviceUnits.get(i); 

            if (currentUnit.getServiceType().equals(serviceType) && currentUnit.isAvailable()) { // is Same service type and available?
                
                System.out.println("Found available: " + serviceType + "!, Dispatching Unit: " + currentUnit.getUnitId() + " for flight: " + flight.getFlightId());
                
                currentUnit.provideService(flight);
                flight.addAssignedServiceToList(currentUnit);
                
                return;  // Leave so we don't add similar services
            }
        }





        // unit is busy now wait
        // System.out.println("Service [" + serviceType + "] is BUSY! Flight [" + flight.getFlightId() + "] is waiting at gate " + flight.getAssignedGateId());
    }

    public void releaseGate(Gate g){
        g.removeFlight();
        System.out.println("Gate: " + g.getGateId() + " is free now!");

        if (!waitingQueue.isEmpty()){ 
            Flight nextFlight = waitingQueue.poll(); 
            System.out.println("Ready to serve flight " + nextFlight.getFlightId());
            assignFlightToGate(nextFlight);
        }
    }

    public void addServiceUnit(ServiceUnit unit) {
        this.serviceUnits.add(unit);
        // System.out.println("Added new service unit: " + unit.getServiceType() + " ID: " + unit.getUnitId());
    }

    public void receiveFlight(Flight flight) {
        flights.add(flight);
        flight.land(timeLine);
        assignFlightToGate(flight);
    }

    public void dispatchFlight(Flight flight) {
        // flight.depart(); 
        // releaseGate(flight.getAssignedGate());
        
        Gate gateToRelease = flight.getAssignedGate(); 
        flight.depart(); 
        if (gateToRelease != null) {
            releaseGate(gateToRelease);
        }
    }

    public void calculateWaitingTime(){
        for (int i = 0; i < this.flights.size(); i++) {
            Flight f = this.flights.get(i);
            this.totalWaitingTime += f.getFlightTotalWaitingTime();
        }

        this.averageWaitTime = this.totalWaitingTime / this.flights.size();
    }

    public void calculateTotalCost(){
        this.totalCost = 0.0; 

        for (int i = 0; i < this.serviceUnits.size(); i++) {
            ServiceUnit currentUnit = this.serviceUnits.get(i);
            
            // check if exist in served units history
            if (!currentUnit.getPlanesServedHistory().isEmpty()) { 
                this.totalCost += currentUnit.getCost();
            } 
        }
    }

    public double getTotalCost() {
        return totalCost;
    }
    

    public double getCurrentTime() {
        return timeLine;
    }
}