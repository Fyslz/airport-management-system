package main;

import javafx.application.Application;
import models.Airport;

public class Main {
    public static void main(String[] args) {
        Airport myAirport = new Airport(8);
        myAirport.run();
    }
}