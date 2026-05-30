package main;

import models.Airport;

public class Main {
    public static void main(String[] args) {
        // بوابة واحدة فقط لاستفزاز نظام الطابور!
        Airport myAirport = new Airport(1);
        
        myAirport.run();
    }
}