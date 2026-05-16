package models;

public class PassengerPlane extends Flight {

    private int passengerCount;
    
    public PassengerPlane(String flightId, double flightArrivalTime, int priority, int passengerCount) {
    
        super(flightId, flightArrivalTime, priority); // inheriting Flight's class contructor
        
        this.passengerCount = passengerCount;
    }
    @Override
    public void requestService(String serviceType){
        System.out.println("Passenger Plane [" + getFlightId() + "]: Requesting " + serviceType + "service.");
        addServiceToList(serviceType);
    }    

    // -------------------------------------
    // Additional Getters (Added by Fysl)
    // -------------------------------------
    public int getPassengerCount() {
        return passengerCount;
    }
    
}
