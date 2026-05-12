package models;
import java.util.List;
import interfaces.Serviceable;

public abstract class Flight implements Serviceable{
    private String flightld;
    private double flightArrivalTime;
    private String flightArrivalStatus;
    private int priority; 
    private double waitingTime;
    private int assignedGate;
    private List<String> requiredServices;

    public void land(){

    }   

    public void depart(){

    }

    public void requestService(String serviceType){

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


}
