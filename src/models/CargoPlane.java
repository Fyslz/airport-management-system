package models;

public class CargoPlane extends Flight {
    
    private double cargoWeight;

    public CargoPlane(String flightId, double flightArrivalTime, int priority, double cargoWeight) {
    
        super(flightId, flightArrivalTime, priority); // inheriting Flight's class contructor
        
        this.cargoWeight = cargoWeight;
    }
    @Override
    public void requestService(String serviceType){
        System.out.println("Cargo Plane [" + getFlightId() + "]: Requesting " + serviceType + "service.");
        addServiceToList(serviceType);
    }


    // -------------------------------------
    // Additional Getters (Added by Fysl)
    // -------------------------------------
    public double getCargoWeight() {
        return cargoWeight;
    }
}
