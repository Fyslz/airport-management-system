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
    private int numberOfGates; // ___ No need for this variable rn because we replace it with gate class
    private boolean[] gateOccupied; // 0 is free - 1 is busy
    private List<Flight> flights; 
    private List<ServiceUnit> serviceUnits;
    private Queue<Flight> waitingQueue;
    private double totalWaitingTime;
    private double totalCost;
    private double currentTime;
    private List<Gate> airportGates;

    private static final String[] ALL_SERVICES = {
        "Ambulance", "FuelTruck", "CleaningCrew", "BaggageHandler", 
        "CateringTruck", "Stairs", "PassengerBus", "PushbackTug", 
        "FireTruck", "MaintenanceService", "CustomsService", "SpecialAssistance"
    };

    public Airport(int numberOfGates){
        this.numberOfGates = numberOfGates; // ___ No need for this variable rn
        this.gateOccupied = new boolean[numberOfGates];
        this.flights = new ArrayList<>();
        this.serviceUnits = new ArrayList<>();
        this.waitingQueue = new LinkedList<>();
        this.totalWaitingTime = 0.0;
        this.totalCost = 0.0;
        this.currentTime = 0.0;

        this.airportGates = new ArrayList<>();
    }

    public void generateFlights(){
        addServiceUnit(new Ambulance(101));
         addServiceUnit(new Ambulance(102));
        addServiceUnit(new FuelTruck(201));
         addServiceUnit(new FuelTruck(202));
        addServiceUnit(new CleaningCrew(301));
         addServiceUnit(new CleaningCrew(302));
        addServiceUnit(new BaggageHandler(401));
         addServiceUnit(new BaggageHandler(402));
        addServiceUnit(new CateringTruck(501));
         addServiceUnit(new CateringTruck(502));
        addServiceUnit(new Stairs(601));
         addServiceUnit(new Stairs(602));
        addServiceUnit(new PassengerBus(701));
         addServiceUnit(new PassengerBus(702));
        addServiceUnit(new PushbackTug(801));
         addServiceUnit(new PushbackTug(802));
        addServiceUnit(new FireTruck(901));
         addServiceUnit(new FireTruck(902));
        addServiceUnit(new MaintenanceService(1101));
         addServiceUnit(new MaintenanceService(1102));
        addServiceUnit(new CustomsService(1201));
         addServiceUnit(new CustomsService(1202));
        addServiceUnit(new SpecialAssistance(1301));
         addServiceUnit(new SpecialAssistance(1302));



        Flight[] p = new Flight[10];
        p[0] = new PassengerPlane("SV111", 10.0, 1, 150);
        p[1] = new CargoPlane("XY222", 10.5, 2, 2000.0);
        p[2] = new PassengerPlane("EK333", 11.0, 3, 300);
        p[3] = new CargoPlane("QR444", 11.5, 1, 1500.0);
        p[4] = new PassengerPlane("GF555", 12.0, 2, 180);
        p[5] = new PassengerPlane("WY666", 12.5, 3, 220);
        p[6] = new CargoPlane("MS777", 13.0, 1, 3500.0);
        p[7] = new PassengerPlane("TK888", 13.5, 2, 250);
        p[8] = new PassengerPlane("BA999", 14.0, 3, 280);
        p[9] = new CargoPlane("LH100", 14.5, 1, 4000.0);

        System.out.println("\n--- Generating Random Service Requests ---");
        for (int i = 0; i < 10; i++) {
            assignRandomServices(p[i]);
        }
        
        System.out.println("\n--- Receiving Flights ---");
        for (int i = 0; i < 10; i++) {
            receiveFlight(p[i]);
        }
    }

    public void run(){
        System.out.println("=== AIRPORT SIMULATION STARTED ===");
        generateFlights();
        
        int totalFlights = flights.size();
        int departedFlightsCount = 0;

        System.out.println("\n--- Processing Airport Dynamics ---");
        
        while (departedFlightsCount < totalFlights) {
            
            for (int i = 0; i < totalFlights; i++) {
                Flight f = flights.get(i);
                boolean isDeparted;
                if (f.getFlightArrivalStatus().equals("Departed")){
                    isDeparted = true;}
                else{
                    isDeparted = false;
                }
                if (f.getAssignedGate() != -1 && !isDeparted) {
                    List<String> currentRequests = f.getRequestedServices();
                    for (int j = 0; j < currentRequests.size(); j++) {
                        assignServiceUnit(f, currentRequests.get(j));
                    }
                }
            }

            for (int i = 0; i < flights.size(); i++) {
                Flight f = flights.get(i);
                boolean isDeparted;
                if (f.getFlightArrivalStatus().equals("Departed")){
                    isDeparted = true;}
                else{
                    isDeparted = false;
                }
                if (f.getAssignedGate() != -1 && !isDeparted) {
                    if (f.getAssignedUnits().size() == f.getRequestedServices().size()) {
                        System.out.println("\nFlight [" + f.getFlightId() + "] received all requested services. Departing...");
                        
                        try {Thread.sleep(3000);} 
                        catch (InterruptedException e) {}

                        List<ServiceUnit> units = f.getAssignedUnits(); // freeing all flight's services
                        for (int k = 0; k < units.size(); k++) {
                            units.get(k).setAvailable(true);
                            System.out.println("   -> Service [" + units.get(k).getServiceType() + " ID: " + units.get(k).getUnitId() + "] is now FREE.");
                        }

                        dispatchFlight(f);
                        departedFlightsCount++;
                        System.out.println("--------------------------------------------------");
                        break;
                    }
                }
            }

            try { Thread.sleep(1000); }
            catch (InterruptedException e) {
            }
        }
        
        printResults();
    }

    public void printResults() {
        System.out.println("\n==================================================");
        System.out.println("                 FINAL AIRPORT REPORT                ");
        System.out.println("==================================================");
        System.out.println("Flight Statistics:");
        System.out.println("   - Total Flights Processed: " + flights.size());
        System.out.println("\nWaiting Time Info:");
        calculateWaitingTime();
        System.out.println("\nFinancial & Services:");
        System.out.println("   - Total Cost of Services: $" + totalCost);
        System.out.println("   - Available Service Units Now: " + ServiceUnit.getAvailableServiceUnits()); 
        System.out.println("==================================================");
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
            if (!this.airportGates.get(i).getIsAvailable()){
                this.airportGates.get(i).setAvailable(true);
                flight.setAssignedGate(this.airportGates.get(i));
                System.out.println("Flight: " + flight.getFlightId() + " to the gate: " + flight.getAssignedGate());
                return;
            }
        }
        waitingQueue.add(flight);
        System.out.println("Flight: " + flight.getFlightId() + " is waiting for a free gate...");
        // TODO: Add waiting time here
    }
    
    public void assignServiceUnit(Flight flight, String requiredServiceType){
        if (!flight.getAssignedGate().getIsAvailable()){ // flight.getAssignedGate().getIsAvailable() will return boolean
            return;
        }
        for (int i = 0; i < flight.getAssignedUnits().size(); i++) {
            ServiceUnit u = flight.getAssignedUnits().get(i);
            if (u.getServiceType().equals(requiredServiceType)) { 
                return;
            }
        }

        for (int i = 0; i < serviceUnits.size(); i++) {
            ServiceUnit unit = serviceUnits.get(i); 
            if (unit.getServiceType().equals(requiredServiceType) && unit.isAvailable()) {
                System.out.println("Found available: " + requiredServiceType + "!, Dispatching Unit: " + unit.getUnitId() + " for flight: " + flight.getFlightId());
                unit.provideService(flight);  
                flight.getAssignedUnits().add(unit);  // will save the unit to the acitve units (method will be changed)
                calculateTotalCost(unit);
                return;
            }
        }
        System.out.println("Service [" + requiredServiceType + "] is BUSY! Flight [" + flight.getFlightId() + "] is waiting at gate " + flight.getAssignedGate());
    }


    public void releaseGate(Gate g){
        g.setAvailable(false);
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
        flight.land();
        assignFlightToGate(flight);
    }

    public void dispatchFlight(Flight flight) {
        int gateToRelease = flight.getAssignedGate();
        flight.depart(); 
        if (gateToRelease != -1) {
            releaseGate(gateToRelease);
        }
    }

    public void calculateWaitingTime(){
        if (waitingQueue.isEmpty()) {
            System.out.println("No flights in the waiting queue.");
            return;
        }

        int numberOfWaitingFlights = waitingQueue.size();
        double estimatedWaitTimePerFlight = 15.0; 
        
        totalWaitingTime = numberOfWaitingFlights * estimatedWaitTimePerFlight;

        System.out.println("There are " + numberOfWaitingFlights + " flights waiting.");
        System.out.println("Estimated total waiting time = " + totalWaitingTime + " mins.");
    }

    public void calculateTotalCost(ServiceUnit unit){
        this.totalCost += unit.getCost();
    }

    public double getTotalCost() {
        return totalCost;
    }
    

    public double getCurrentTime() {
        return currentTime;
    }
}